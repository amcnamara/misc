This is an implementation of a linked-list in Python. Linked-lists aren't idiomatic and should generally be avoided in Python; but can be useful for modelling certain problems, as well as for interview practice. The implementation is straightforward, and usage is as follows:

```python
## Appending objects to the linked list is easily accomplished with the addition operator
## Note: Adds are in place, my_ll + object is the same as my_ll += object
my_ll = LList()
some_object = object()
my_ll += 1.61803399
my_ll += someobject
my_ll += "some-string"
print my_ll
```
> 1.61803399 -> <object object at 0x1002af0b0> -> 'some-string'

```python
## Reverse the LL in place
my_ll.reverse()
print my_ll
```
> 'some-string' -> <object object at 0x1002af0b0> -> 1.61803399

```python
## Retrieve a sequence of nodes which contain a specified datum
nodes = my_ll.getNodes(some_object)
print nodes
```
> [<__main__.Node object at 0x1004a46d0>]

```python
## Remove a node with sub
my_ll -= nodes[0]
print my_ll
```
> 'some-string' -> 1.61803399

```python
## Insert a new node _before_ the given node, node defaults to head (to easily append to tail, see above)
my_ll.insert('new-head-string', my_ll.head)
print my_ll
```
> 'new-head-string' -> 'some-string' -> 1.61803399

Notes:
- Operations/mutations on the LList are in place (specifically: add, insert, sub, reverse)
- Reverse is single pass, so pretty efficient (O(n))
- Iterator uses tortoise-vs-hare cycle detection, and throws an exception if a cycle is found
- Pointers to both head and tail are maintained in the list, implementing a Stack or Queue based on this implementation is trivial and left as an excercise to the reader.  ;)