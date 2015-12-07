#!/usr/bin/env python3


def make_array(text):
    return text.strip().split()


def multiway_insert(root, text):
    global NODE_CREATED
    if len(text) == 0:
        NODE_CREATED += 1
        return True
    if root is None:
        NODE_CREATED += 1
        root = [None] * 3

    idx = ord(text[0]) - ord('1')
    root[idx] = multiway_insert(root[idx], text[1:])

    return root


class TernerayTrie:
    def __init__(self, v):
        self.v = v
        self.l = self.r = self.m = None


def ternary_insert(root, text, depth):
    if len(text) == 0:
        return root

    global TREE_DEPTH
    if depth > TREE_DEPTH:
        TREE_DEPTH = depth

    idx = ord(text[0]) - ord('0')
    if root is None:
        root = TernerayTrie(idx)

    if idx < root.v:
        root.l = ternary_insert(root.l, text, depth + 1)
    elif idx == root.v:
        root.m = ternary_insert(root.m, text[1:], depth + 1)
    elif idx > root.v:
        root.r = ternary_insert(root.r, text, depth + 1)
    return root


def ternary_print(root, prefix):
    if root is None:
        return
    print(prefix, '[' + str(root.v) + ']')
    print(prefix, 'l')
    ternary_print(root.l, prefix + '  ')
    print(prefix, 'm')
    ternary_print(root.m, prefix + '  ')
    print(prefix, 'r')
    ternary_print(root.r, prefix + '  ')


def solve_q1(arr):
    global NODE_CREATED
    NODE_CREATED = 0
    root = None
    for text in arr:
        root = multiway_insert(root, text)
    print(NODE_CREATED)


def solve_q2(arr):
    root = None
    for text in arr:
        global TREE_DEPTH
        TREE_DEPTH = 0
        root = ternary_insert(root, text, 0)
        print(TREE_DEPTH, end=' ')
    print()


q1 = '132 2133 3323 22 3232 3231 2331'
q2 = '243 552 143 531 331 234 442 '

solve_q1(make_array(q1))
solve_q2(make_array(q2))
