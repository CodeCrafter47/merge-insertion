package data_structures

private const val min_elements = 2
private const val max_elements = 4

/**
 * Data structure similar to the Rope data structure used in text processing.
 *
 * This is based on compute.a BTree and allows for storing arbitrary types.
 *
 * Other tree based data structures (e.g. RRB-Trees) could be used instead.
 */
class Vector<T> {

    private var root : Node<T> = Node.Leaf();

    fun insertAt(index: Long, element: T) {
        if (index < 0)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        if (index > root.size)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        root.insertAt(index, element)
        if (root.numChildren() > max_elements) {
            val (a, b) = root.split()
            val newRoot = Node.Inner<T>()
            newRoot.children[0] = a
            newRoot.children[1] = b
            newRoot.num_children = 2
            newRoot.size = a.size + b.size
            root = newRoot
        }
    }

    fun removeAt(index: Long) : T {
        if (index < 0)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        if (index >= root.size)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        val result = root.removeAt(index)
        if (root is Node.Inner && (root as Node.Inner).numChildren() == 1L) {
            root = (root as Node.Inner).children[0] as Node<T>
        }
        return result
    }

    fun forEachIndexed(callback: (Long, T) -> Unit) {
        root.forEachIndexed(0, root.size, callback)
    }

    fun forEachIndexed(max: Long, callback: (Long, T) -> Unit) {
        root.forEachIndexed(0, max, callback);
    }

    fun get(index: Long): T {
        if (index < 0)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        if (index >= root.size)
            throw IndexOutOfBoundsException("index $index size ${root.size}")
        return root.get(index)
    }

    fun size() = root.size
    fun append(element: T) {
        insertAt(size(), element)
    }

    private sealed class Node<T> {
        abstract fun insertAt(index: Long, element: T)
        abstract fun split() : Pair<Node<T>, Node<T>>
        abstract fun removeAt(index: Long): T
        abstract fun numChildren(): Long
        abstract fun giveToRight(other: Node<T>)
        abstract fun giveToLeft(other: Node<T>)

        abstract fun merge(other: Node<T>)
        abstract fun forEachIndexed(offset: Long, max: Long, callback: (Long, T) -> Unit)
        abstract fun get(index: Long): T

        var size: Long = 0;
        class Inner<T> : Node<T>() {

            val children = Array<Node<T>?>(max_elements + 1) { null }
            var num_children = 0

            override fun numChildren(): Long {
                return num_children.toLong()
            }

            override fun insertAt(index: Long, element: T) {
                var indexInChild = index
                var i = 0
                val max = num_children
                while (i < max) {
                    val child = children[i] as Node<T>
                    val elementsInChild = child.size
                    if (indexInChild <= elementsInChild) {
                        child.insertAt(indexInChild, element)
                        if (child.numChildren() > max_elements) {
                            val (a, b) = child.split()
                            children[i] = a
                            var j = num_children - 1
                            while (j > i) {
                                children[j + 1] = children[j]
                                j--
                            }
                            children[i + 1] = b
                            num_children++
                        }
                        break
                    }
                    indexInChild -= elementsInChild
                    i++
                }
                size++
            }

            override fun split(): Pair<Node<T>, Node<T>> {
                val a = Inner<T>()
                val b = Inner<T>()
                val n = children.size / 2
                for (i in 0 until n)
                    a.children[i] = children[i]
                a.num_children = n
                a.size = a.children.map { it?.size ?: 0 }.sum()
                for (i in n until num_children)
                    b.children[i - n] = children[i]
                b.num_children = num_children - n
                b.size = b.children.map { it?.size ?: 0 }.sum()
                return Pair(a, b)
            }

            override fun removeAt(index: Long): T {
                var indexInChild = index
                var i = 0
                val max = num_children
                while (i < max) {
                    val child = children[i] as Node<T>
                    val elementsInChild = child.size
                    if (indexInChild < elementsInChild) {
                        val result = child.removeAt(indexInChild)
                        size--
                        if (child.numChildren() < min_elements) {
                            if (i > 0) {
                                val leftSibling = children[i - 1] as Node<T>
                                if (child.numChildren() + leftSibling.numChildren() <= max_elements) {
                                    leftSibling.merge(child)
                                    var j = i
                                    while (j < num_children) {
                                        children[j] = children[j + 1]
                                        j++
                                    }
                                    num_children--
                                } else {
                                    leftSibling.giveToRight(child)
                                }
                            } else if (i < num_children - 1) {
                                val rightSibling = children[i + 1] as Node<T>
                                if (child.numChildren() + rightSibling.numChildren() <= max_elements) {
                                    child.merge(rightSibling)
                                    var j = i + 1
                                    while (j < num_children) {
                                        children[j] = children[j + 1]
                                        j++
                                    }
                                    num_children--
                                } else {
                                    rightSibling.giveToLeft(child)
                                }
                            } else {

                            }
                        }
                        return result
                    }
                    indexInChild -= elementsInChild
                    i++
                }
                throw AssertionError()
            }

            override fun merge(other: Node<T>) {
                if (other !is Inner<T>)
                    throw AssertionError()
                for (i in 0 until other.num_children)
                    children[num_children + i] = other.children[i]
                num_children += other.num_children
                size += other.size
            }

            override fun giveToRight(other: Node<T>) {
                if (other !is Inner<T>)
                    throw AssertionError()
                val node = children[--num_children] as Node<T>
                children[num_children] = null
                size -= node.size
                other.size += node.size
                for (i in other.num_children downTo 1)
                    other.children[i] = other.children[i-1]
                other.children[0] = node
                other.num_children++
            }

            override fun giveToLeft(other: Node<T>) {
                if (other !is Inner<T>)
                    throw AssertionError()
                val node = children[0] as Node<T>
                for (i in 0 until num_children)
                    children[i] = children[i + 1]
                size -= node.size
                other.size += node.size
                other.children[other.num_children] = node
                other.num_children++
                num_children--
            }

            override fun forEachIndexed(offset: Long, max: Long, callback: (Long, T) -> Unit) {
                var cOffset = offset
                var i = 0
                while ( i < num_children) {
                    val child = children[i] as Node<T>
                    if (cOffset >= max)
                        break
                    child.forEachIndexed(cOffset, max, callback)
                    cOffset += child.size
                    i++
                }
            }

            override fun get(index: Long): T {
                var indexInChild = index
                val max = numChildren()
                var i = 0
                while ( i < max) {
                    val child = children[i] as Node<T>
                    val elementsInChild = child.size
                    if (indexInChild < elementsInChild) {
                        return child.get(indexInChild)
                    }
                    indexInChild -= elementsInChild
                    i++
                }
                throw AssertionError()
            }
        }

        class Leaf<T> : Node<T>() {
            val elements : MutableList<T> = ArrayList()

            override fun numChildren(): Long {
                return elements.size.toLong()
            }

            override fun insertAt(index: Long, element: T) {
                elements.add(index.toInt(), element)
                size++
            }

            override fun split(): Pair<Node<T>, Node<T>> {
                val a = Leaf<T>()
                val b = Leaf<T>()
                val n = size / 2
                a.elements.addAll(elements.take(n.toInt()))
                a.size = n
                b.elements.addAll(elements.takeLast((size - n).toInt()))
                b.size = size - n
                return Pair(a, b)
            }

            override fun removeAt(index: Long): T {
                size--
                return elements.removeAt(index.toInt())
            }

            override fun merge(other: Node<T>) {
                if (other !is Leaf<T>)
                    throw AssertionError()
                elements.addAll(other.elements)
                size += other.size
            }

            override fun giveToRight(other: Node<T>) {
                if (other !is Leaf<T>)
                    throw AssertionError()
                val node = elements.removeAt(elements.size - 1)
                size -= 1
                other.size += 1
                other.elements.add(0, node)
            }

            override fun giveToLeft(other: Node<T>) {
                if (other !is Leaf<T>)
                    throw AssertionError()
                val node = elements.removeAt(0)
                size -= 1
                other.size += 1
                other.elements.add(node)
            }

            override fun forEachIndexed(offset: Long, max: Long, callback: (Long, T) -> Unit) {
                for ((index, element) in elements.withIndex()) {
                    if (offset + index >= max)
                        break
                    callback(offset + index, element)
                }
            }

            override fun get(index: Long): T {
                return elements[index.toInt()]
            }
        }
    }
}