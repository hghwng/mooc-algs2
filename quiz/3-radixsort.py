#!/usr/bin/env python3


def make_arr(text):
    return text.strip().split(' ')


def print_arr(arr):
    for t in arr:
        print(t, end=' ')
    print()


def solve_q1(arr, time):
    for t in range(len(arr[0]) - 1, time - 1, -1):
        arr = sorted(arr, key=lambda x: x[t])
    return arr


def msd_radix_sort(arr, start, end, depth):
    if end - start <= 1:
        return

    global msd_radix_sort_left
    if msd_radix_sort_left <= 0:
        return
    msd_radix_sort_left -= 1

    arr[start:end] = sorted(arr[start:end], key=lambda x: x[depth])

    pre_n = start
    pre_v = arr[pre_n][depth]
    for i in range(start, end):
        if arr[i][depth] != pre_v:
            pre_v = arr[i][depth]
            msd_radix_sort(arr, pre_n, i, depth + 1)
            pre_n = i
    msd_radix_sort(arr, pre_n, end, depth + 1)


def solve_q2(arr, time):
    global msd_radix_sort_left
    msd_radix_sort_left = time
    msd_radix_sort(arr, 0, len(arr), 0)
    return arr


def solve_q3(arr):
    k = arr[0][0]
    l = 0
    m = l
    h = len(arr) - 1

    while m <= h:
        v = arr[m][0]
        if v < k:
            arr[m], arr[l] = arr[l], arr[m]
            m += 1
            l += 1
        elif v == k:
            m += 1
        else:  # arr[m] > k
            arr[m], arr[h] = arr[h], arr[m]
            h -= 1
    return arr

q1 = ' 4322 4441 1244 3122 1332 2131 4431 3113 2244 1241'
q2 = ' 1324 3314 1122 3112 4423 3321 3344 4223 1412 1344 4314 4412 1333 2323 3243 '
q3 = '  5552 5255 3462 2614 6432 5252 6543 6152 5156 5434 '

print_arr(solve_q1(make_arr(q1), 2))
print_arr(solve_q2(make_arr(q2), 3))
print_arr(solve_q3(make_arr(q3)))
