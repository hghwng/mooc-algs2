#!/usr/bin/env python3
def make_graph(text):
    graph = {}
    edges = []
    for line in text.split('\n'):
        val = -1
        v0 = v1 = None
        for i in range(len(line)):
            char = line[i]
            if char in ' ->':
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
            graph[v0][v1] = val
            edges.append((val, v0, v1))

    return graph, edges


def make_array(text):
    arr = []
    for i in text:
        if ord('A') <= ord(i) <= ord('Z'):
            arr.append(ord(i) - ord('A'))
    return arr


def dijkstra(graph, start, end):
    SIZE = 8
    MAX = 1000
    dist = [MAX] * SIZE
    to_reach = [MAX] * SIZE
    dist[start] = 0
    reached = set([start])
    src = start

    while len(reached) != SIZE:
        for dst in graph[src]:
            if dst in reached:
                to_reach[dst] = MAX
            else:
                to_reach[dst] = min(to_reach[dst], graph[src][dst])
        dst = min(range(len(to_reach)), key=to_reach.__getitem__)

        for i in graph[src]:
            newdist = dist[src] + graph[src][i]
            if newdist < dist[i]:
                dist[i] = newdist

        if src == end:
            for v in dist:
                print('-' if v == MAX else v, end=' ')
            print()
            return

        reached.add(dst)
        to_reach[dst] = MAX
        src = dst


def dap_sp(graph, top, end):
    SIZE = 8
    MAX = 1000
    dist = [MAX] * SIZE
    dist[top[0]] = 0

    for src in top:
        for dst in graph[src]:
            newdist = dist[src] + graph[src][dst]
            if newdist < dist[dst]:
                dist[dst] = newdist
        if src == end:
            for v in dist:
                print('-' if v == MAX else v, end=' ')
            print()
            return


def bellman_ford(graph, start):
    SIZE = 8
    MAX = 1000
    dist = [MAX] * SIZE
    dist[start] = 0

    for time in range(3):
        for src in graph:
            for dst in graph[src]:
                newdist = dist[src] + graph[src][dst]
                if newdist < dist[dst]:
                    dist[dst] = newdist

    for v in dist:
        print('-' if v == MAX else v, end=' ')
    print()


q1 = '''
    A->E     2
    B->A    37
    B->C    14
    B->E    41
    C->D    10
    D->H     4
    F->B    32
    F->C    38
    F->E    79
    G->C    65
    G->F    19
    G->H     4
    H->C    57
'''
q1_start = 'G'
q1_end = 'C'

q2 = '''
    A->E    31
    A->F     1
    B->A    21
    B->F    16
    C->B    22
    C->F    41
    C->G     5
    D->C    35
    D->G    49
    D->H    50
    F->E    31
    G->F    34
    G->H     2
'''
q2_top = '    D C G H B A F E'
q2_end = 'A'

q3 = '''
    A->E     5
    B->A    20
    B->C     3
    D->C    21
    F->A    50
    F->B    24
    F->E     7
    F->C     1
    G->F    34
    G->C    32
    H->D    18
    H->C    37
    H->G    10
'''
q3_start = 'H'

dijkstra(make_graph(q1)[0],
         ord(q1_start) - ord('A'),
         ord(q1_end) - ord('A'))

dap_sp(make_graph(q2)[0],
       make_array(q2_top),
       ord(q2_end) - ord('A'))

bellman_ford(make_graph(q3)[0],
             ord(q3_start) - ord('A'))
