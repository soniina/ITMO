import math

import numpy as np
from matplotlib import pyplot as plt
import scipy.optimize


def lagrange_polynomial(xs, ys, x):
    result = 0.0

    for i in range(n):
        term = ys[i]
        for j in range(n):
            if i != j:
                term *= (x - xs[j]) / (xs[i] - xs[j])
        result += term

    return result


def newton_polynomial_with_divided_differences(xs, x):
    diff_table = ys.copy()
    for i in range(1, len(xs)):
        for j in range(len(xs) - 1, i - 1, -1):
            diff_table[j] = (diff_table[j] - diff_table[j - 1]) / (xs[j] - xs[j - i])

    result = diff_table[-1]
    for i in range(n - 2, -1, -1):
        result = result * (x - xs[i]) + diff_table[i]

    return result


def make_table_of_finite_differences(ys):
    diff_table = [ys]

    for order in range(1, len(ys)):
        prev_row = diff_table[-1]
        new_row = [prev_row[i + 1] - prev_row[i] for i in range(len(prev_row) - 1)]
        diff_table.append(new_row)

    return diff_table


def newton_polynomial_with_finite_differences(xs, x, diff_table, h):
    n = len(xs)
    middle = (xs[0] + xs[-1]) / 2

    closest_index = 0
    min_dist = abs(x - xs[0])
    for i in range(1, n):
        dist = abs(x - xs[i])
        if dist < min_dist:
            min_dist = dist
            closest_index = i

    if x <= middle:
        t = (x - xs[closest_index]) / h
        result_closest = diff_table[0][closest_index]
        mult = 1.0

        for i in range(1, len(diff_table)):
            mult *= (t - i + 1)
            if 0 <= closest_index < len(diff_table[i]):
                result_closest += mult * diff_table[i][closest_index] / math.factorial(i)

        t = (x - xs[0]) / h
        result = diff_table[0][0]
        mult = 1.0

        for i in range(1, len(diff_table)):
            mult *= (t - i + 1)
            result += mult * diff_table[i][0] / math.factorial(i)

    else:
        t = (x - xs[closest_index]) / h
        result_closest = diff_table[0][closest_index]
        mult = 1.0

        for i in range(1, len(diff_table)):
            mult *= (t + i - 1)
            if 0 <= closest_index - i < len(diff_table[i]):
                result_closest += mult * diff_table[i][closest_index - i] / math.factorial(i)

        t = (x - xs[n - 1]) / h
        result = diff_table[0][n - 1]
        mult = 1.0

        for i in range(1, len(diff_table)):
            mult *= (t + i - 1)
            if 0 <= n - i - 1 < len(diff_table[i]):
                result += mult * diff_table[i][n - i - 1] / math.factorial(i)

    return result, result_closest


def plot_interpolation(xs, ys, x_interp, y_interp, method_name, x_plot, y_plot):
    plt.figure()
    plt.plot(x_plot, y_plot, color='lightgray', linewidth=2)
    plt.plot(xs, ys, 'o', label='Узлы интерполяции', color='blue')
    plt.plot(x_interp, y_interp, 'ro', label=f'Интерполяция в x={x_interp:.3f}')

    plt.title(f'{method_name}')
    plt.xlim(x_min, x_max)
    plt.xlabel("x")
    plt.ylabel("y")
    plt.legend()
    plt.tight_layout()
    plt.grid(True, linestyle='--', alpha=0.8)
    plt.show()


# --- Исходные данные ---

n = 0
x = 0
table = []
funcs = {
    '1': lambda x: np.sin(x),
    '2': lambda x: x ** 3,
    '3': lambda x: np.exp(x)
}

# --- Ввод данных ---

print('Вычислительная математика. Лабораторная работа 5: "Интерполяция функции".\n')

print("Как вы хотите ввести исходные данные?\n1 - с консоли\n2 - с файла\n3 - на основе функции")
v = input()
while v not in {'1', '2', '3'}:
    print('Выберите первый (1), второй (2) или третий (3) вариант')
    v = input()
print()

if v == '1':
    n = int(input("Сколько точек вы хотите ввести? "))
    while n < 0:
        n = int(input("Выберите значение больше 0: "))
    print("Введите построчно значения x и y через пробел:")
    for i in range(n):
        table.append(tuple(map(float, input().split())))
elif v == '2':
    print("Введите название файла:")
    with open(input().strip(), 'r') as file:
        lines = file.readlines()
        n = int(lines[0])
        for i in range(n):
            table.append(tuple(map(float, lines[i + 1].split())))
else:
    print('1: sin(x)\n2: x^3\n3: e^x')
    func_number = input('Выберите функцию: ')
    while func_number not in {'1', '2', '3'}:
        func_number = input('Выберите первую (1), вторую (2) или третью (3) функцию: ')
    print("\nВведите исследуемый интервал:")
    x0 = float(input('x_0 = '))
    xn = float(input('x_n = '))
    n = int(input("Введите количество точек на интервале: "))
    xs = np.linspace(x0, xn, n)
    for x, y in zip(xs, funcs[func_number](xs)):
        table.append((x, y))
x = float(input('Введите точку интерполяции: '))
if v == '3':
    print(f"Реальное значение функции в точке интерполяции:", funcs[func_number](x))
table = sorted(table)
print()

# --- Интерполяция ---

xs = [xi for xi, _ in table]
ys = [yi for _, yi in table]
h = xs[1] - xs[0]

x_min_raw = min(min(xs), x)
x_max_raw = max(max(xs), x)
padding = 0.05 * (x_max_raw - x_min_raw)
x_min = x_min_raw - padding
x_max = x_max_raw + padding
x_plot = np.linspace(x_min, x_max, 1000)

lagrange_result = lagrange_polynomial(xs, ys, x)


plot_interpolation(xs, ys, x, lagrange_result, "Многочлен Лагранжа", x_plot, [lagrange_polynomial(xs, ys, xi) for xi in x_plot])

print(f"\nРезультаты интерполяции в точке x = {x}:")
print(f"  1. Многочлен Лагранжа:                          y ≈ {lagrange_result:.8f}")

if not all(abs(xs[i + 1] - xs[i] - h) < 1e-9 for i in range(len(xs) - 1)):
    newton_div_result = newton_polynomial_with_divided_differences(xs, x)
    plot_interpolation(xs, ys, x, newton_div_result, "Многочлен Ньютона c разделенными разностями", x_plot, [newton_polynomial_with_divided_differences(xs, xi) for xi in x_plot])
    print(f"  2. Многочлен Ньютона c разделенными разностями: y ≈ {newton_div_result:.8f}")
    print("\n  Конечные разности используются только на равномерной сетке!\n")
else:
    print("\n  Разделенные разности используются на неравномерной сетке!\n")
    diff_table = make_table_of_finite_differences(ys)
    print("  2. Таблица конечных разностей (Δ^k y):")
    for i, row in enumerate(diff_table):
        print(f"     Δ^{i} y: " + ", ".join(f"{val:.5f}" for val in row))

    newton_fin_result = newton_polynomial_with_finite_differences(xs, x, diff_table, h)
    plot_interpolation(xs, ys, x, newton_fin_result[0], "Многочлен Ньютона с конечными разностями", x_plot, [result[0] for result in (newton_polynomial_with_finite_differences(xs, xi, diff_table, h) for xi in x_plot)])
    plot_interpolation(xs, ys, x, newton_fin_result[1], "Многочлен Ньютона с конеч. разн. (с выбором ближ. слева x)", x_plot, [result[1] for result in (newton_polynomial_with_finite_differences(xs, xi, diff_table, h) for xi in x_plot)])
    print(f"     Многочлен Ньютона с конечными разностями:    y ≈ {newton_fin_result[0]:.8f}")
    if abs(newton_fin_result[0] - newton_fin_result[1]) > 1e-9:
        print(f"     (с выбором ближайшего слева x):          y ≈ {newton_fin_result[1]:.8f}")
