#!/usr/bin/env python3
class UnionSet:
    def __init__(self, n):
        self.arr = []
        for i in range(n):
            self.arr.append(i)

    def query(self, i):
        while self.arr[i] != self.arr[self.arr[i]]:
            self.arr[i] = self.arr[self.arr[i]]
        return self.arr[i]

    def join(self, i, j):
        self.arr[self.query(i)] = self.query(j)

    def same(self, i, j):
        return self.query(i) == self.query(j)


def make_graph(text):
    graph = {}
    edges = []
    for line in text.split('\n'):
        val = -1
        v0 = v1 = None
        for i in range(len(line)):
            char = line[i]
            if char == ' ' or char == '-':
                continue

            if ord('0') <= ord(char) <= ord('9'):
                val = int(line[i:])
                break

            if ord('A') <= ord(char) <= ord('Z'):
                if v0 is None:
                    v0 = ord(char) - ord('A')
                elif v1 is None:
                    v1 = ord(char) - ord('A')

        if val != -1 and v0 is not None and v1 is not None:
            if v0 not in graph:
                graph[v0] = dict()
            if v1 not in graph:
                graph[v1] = dict()
            graph[v0][v1] = graph[v1][v0] = val
            edges.append((val, v0, v1))

    return graph, edges


def kruskal(edges):
    edges.sort(key=lambda x: x[0])
    us = UnionSet(30)
    for edge in edges:
        if us.same(edge[1], edge[2]):
            continue
        us.join(edge[1], edge[2])
        print(edge[0], end=' ')
    print()


def prim(graph, start):
    import queue
    todo = queue.PriorityQueue()
    done = set([start])

    for k, v in graph[start].items():
        todo.put((v, k))

    while not todo.empty():
        elem = todo.get()
        v = elem[0]
        k = elem[1]
        if k in done:
            continue
        done.add(k)
        print(v, end=' ')

        for k, v in graph[k].items():
            if k in done:
                continue
            todo.put((v, k))
    print()

q1 = '''
    F-A    15
    A-B     5
    A-G     3
    B-G     6
    C-B     4
    D-C    17
    I-C    10
    C-H     7
    G-C     2
    E-D    12
    D-I    11
    D-J     9
    J-E    14
    G-F    16
    G-H    13
    H-I     1
    I-J     8
'''

q2 = '''
    B-A      16
    A-F      13
    C-B      12
    B-G      11
    H-B      10
    F-B       3
    C-D       6
    C-H       5
    I-C       2
    D-I      15
    D-E       8
    E-J       9
    I-E       1
    G-F      17
    G-H       7
    I-H       4
    I-J      14
'''

q2_start = 'D'

kruskal(make_graph(q1)[1])
prim(make_graph(q2)[0], ord(q2_start) - ord('A'))
