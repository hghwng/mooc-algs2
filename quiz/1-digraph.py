#!/usr/bin/env python3
from collections import deque


def solve_bfs(g):
    q = deque([0])
    vis = [False] * len(g)
    vis[0] = True

    while len(q):
        elem = q.popleft()
        print(chr(elem + ord('A')), end=' ')
        for i in g[elem]:
            if not vis[i]:
                vis[i] = True
                q.append(i)
    print()


g_tplg_stack = []
g_tplg_vis = []


def solve_tplg_doit(g, i):
    global g_tplg_vis, g_tplg_stack
    if g_tplg_vis[i]:
        return
    g_tplg_vis[i] = True

    for elem in g[i]:
        solve_tplg_doit(g, elem)
    g_tplg_stack.append(i)


def solve_topology_sort(g):
    global g_tplg_vis, g_tplg_stack
    g_tplg_vis = [False] * len(g)
    g_tplg_stack = []
    for i in range(len(g)):
        solve_tplg_doit(g, i)

    g_tplg_stack.reverse()
    for i in g_tplg_stack:
        print(chr(i + ord('A')), end=' ')
    print()


g_scc_vis = []
g_scc_tag = []


def solve_scc_doit(g, i, tag):
    if g_scc_vis[i]:
        return False

    g_scc_vis[i] = True
    g_scc_tag[i] = tag
    for elem in g[i]:
        solve_scc_doit(g, elem, tag)
    return True


def solve_scc(g, seq):
    global g_scc_vis, g_scc_tag
    g_scc_vis = [False] * len(g)
    g_scc_tag = [-1] * len(g)
    tag = 0
    for i in seq:
        if solve_scc_doit(g, i, tag):
            tag += 1

    for i in g_scc_tag:
        print(i, end=' ')
    print()


def make_graph(text):
    graph = {}
    for line in text.split('\n'):
        num_processed = 0
        vertex_id = -1
        for char in line:
            if char[0] < 'A' or char[0] > 'Z':
                continue
            current_id = ord(char) - ord('A')
            if num_processed == 0:
                vertex_id = current_id
                graph[vertex_id] = []
            else:
                graph[vertex_id].append(current_id)

            num_processed += 1
    return graph

q1 = '''
    A:  E B
    B:  F
    C:  D B
    D:  H G
    E:  F B
    F:  G C
    G:  C
    H:  G
'''

q2 = '''
    A:
    B:  C F A E G
    C:  G D
    D:
    E:  A
    F:  G E
    G:  D
    H:  D G
'''

q3 = '''
    A:  G
    B:  A
    C:  B
    D:  H C
    E:  I D
    F:  A
    G:  H B F
    H:  C B
    I:  D H J
    J:  E
'''
q3_seq = 'A B C H D I E J F G'

solve_bfs(make_graph(q1))
solve_topology_sort(make_graph(q2))
solve_scc(make_graph(q3), [(ord(i) - ord('A')) for i in (q3_seq.split(' '))])
