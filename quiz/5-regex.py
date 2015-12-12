#!/usr/bin/env python3


def make_array(text):
    import re
    regex = re.compile('(\d+)->(\d+)')
    pairs = regex.findall(text)
    ret = list()
    for (src, dst) in pairs:
        src = int(src)
        dst = int(dst)
        ret.append((src, dst))
    return ret


def make_transitive_closure(states, eps_trans):
    while True:
        changed = False
        for src in range(len(states)):
            for dst in eps_trans[src]:
                if dst not in states:
                    states.add(dst)
                    changed = True
        if not changed:
            return states


def make_epsilon_transition(regex):
    trans = list(map(lambda x: set(), range(len(regex))))
    stack = []

    for i in range(len(regex)):
        c = regex[i]
        group_begin = i

        if c == '(':
            trans[i].add(i + 1)
            stack.append(i)
        elif c == '|':
            stack.append(i)
        elif c == ')':
            trans[i].add(i + 1)
            top = stack.pop()
            if regex[top] == '(':
                group_begin = top
            elif regex[top] == '|':
                group_begin = stack.pop()
                trans[group_begin].add(top + 1)
                trans[top].add(i)
        elif c == '*':
            trans[i].add(i + 1)

        if i + 1 < len(regex) and regex[i + 1] == '*':
            trans[group_begin].add(i + 1)
            trans[i + 1].add(group_begin)

    return trans


def solve_q1(regex, query):
    eps_trans = make_epsilon_transition(regex)
    states = set()
    states.add(0)
    make_transitive_closure(states, eps_trans)

    for i in query:
        new_states = set()
        for st in states:
            if st == len(regex):
                continue
            if i == regex[st]:
                new_states.add(st + 1)
            states = make_transitive_closure(new_states, eps_trans)

    for i in list(states):
        print(i, end=' ')
    print()


def solve_q2(regex, queries):
    eps_trans = make_epsilon_transition(regex)
    for q in queries:
        if q[1] in eps_trans[q[0]]:
            print('y', end=' ')
        else:
            print('n', end=' ')
    print()

q1_regex = ' ( ( A | ( C * B ) ) * A ) '
q1_query = '     A B B A B C '

q2_regex = '     ( A ( ( C D * ) * | B ) ) '
q2_query = '''



8->3

10->12

7->5

4->9

0->1

2->10

3->8

'''

solve_q1(q1_regex.replace(' ', ''),
         q1_query.replace(' ', ''))
solve_q2(q2_regex.replace(' ', ''), make_array(q2_query))
