import numpy as np
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
import sympy as sp

MAX_ITERATIONS = 150


def print_results(result):
    """Вывод результатов в консоль или файл"""
    if output_number == '2':
        with open(output_filename, 'a') as file:
            file.write(result + '\n')
    else:
        print(result)


def build_graph():
    """Построение графика функции"""
    x = np.linspace(-3, 3, 1000)
    y = np.linspace(-3, 3, 1000)
    X, Y = np.meshgrid(x, y)

    F = f(X, Y)
    G = g(X, Y)

    plt.figure(figsize=(12, 10))

    plt.contour(X, Y, F, levels=[0], colors='blue')
    plt.contour(X, Y, G, levels=[0], colors='red')

    plt.xticks(np.arange(-3, 3, 0.25))
    plt.yticks(np.arange(-3, 3, 0.25))

    proxy_f = Line2D([0], [0], color='blue', label='f(x, y) = 0')
    proxy_g = Line2D([0], [0], color='red', label='g(x, y) = 0')

    plt.axhline(0, color='black', linewidth=1)
    plt.axvline(0, color='black', linewidth=1)
    plt.title(f'Графики системы уравнений ({system_number})')
    plt.xlabel('x')
    plt.ylabel('y')
    plt.legend(handles=[proxy_f, proxy_g])
    plt.grid(True)
    plt.show()


def newtons_method(x0, y0, eps, n = 0):
    """Метод Ньютона"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")

    f_val = -f(x0, y0)
    g_val = -g(x0, y0)

    df_dx = sp.lambdify((x, y), sp.diff(systems[system_number]['f'], x), 'numpy')
    df_dy = sp.lambdify((x, y), sp.diff(systems[system_number]['f'], y), 'numpy')
    dg_dx = sp.lambdify((x, y), sp.diff(systems[system_number]['g'], x), 'numpy')
    dg_dy = sp.lambdify((x, y), sp.diff(systems[system_number]['g'], y), 'numpy')

    J = np.array([
        [df_dx(x0, y0), df_dy(x0, y0)],
        [dg_dx(x0, y0), dg_dy(x0, y0)]
    ])
    F = np.array([f_val, g_val])
    dx, dy = np.linalg.solve(J, F)

    X = x0 + dx
    Y = y0 + dy

    print_results(f"Шаг {n}: x_i = {X:.6f}, y_i = {Y:.6f}, f(x_i, y_i) = {f_val:.6f}, g(x_i, y_i) = {g_val:.6f}, "
          f"|dx| = {abs(dx):.6f}, |dy| = {abs(dy):.6f}, x_(i+1) = {X:.6f}, y_(i+1) = {Y:.6f}")

    if abs(X - x0) <= eps and abs(Y - y0) <= eps:
        return X, x0, Y, y0, n
    return newtons_method(X, Y, eps, n + 1)


print('Вычислительная математика. Лабораторная работа 2_2: "Численное решение системы нелинейных уравнений". '
      'Вариант 13: Метод Ньютона\n')


# --- Исходные данные ---

x0 = 0
y0 = 0
eps = 0


# --- Системы уравнений и их производные ---

x, y = sp.symbols('x y')

systems = {
    '1': {
        'f': sp.sympify(sp.tan(x * y + 0.3) - x**2),
        'g': sp.sympify(0.9 * x**2 + 2 * y**2 - 1),
    },
    '2': {
        'f': sp.sympify(x + sp.sin(y) + 0.4),
        'g': sp.sympify(2 * y - sp.cos(x + 1)),
    }
}


# --- Ввод данных ---

print('1: { tg(xy + 0.3) = x^2; 0.9x^2 + 2y^2 = 1 }\n2: { x + siny = -0.4; 2y - cos(x + 1) = 0 }')
system_number = input('Выберите систему уравнений: ')

while system_number not in {'1', '2', '3', '4'}:
    system_number = input('Выберите первую (1) или вторую (2) систему уравнений: ')

f = sp.lambdify((x, y), systems[system_number]['f'], "numpy")
g = sp.lambdify((x, y), systems[system_number]['g'], "numpy")

build_graph()

print('\nКак вы хотите ввести исходные данные(начальные приближение к корню и погрешность вычисления)?')
print("1: с консоли\n2: с файла")
input_number = input()
while input_number not in {'1', '2'}:
    input_number = input('Выберите первый (1) или второй (2) вариант: ')

if input_number == '1':
    print("\nВведите начальные приближения:")
    x0 = float(input('x0 = '))
    y0 = float(input('y0 = '))
    print("Введите погрешность вычисления:")
    eps = float(input('ε = '))
else:
    input_filename = input("Введите название файла: ").strip()
    with open(input_filename, 'r') as file:
        lines = file.readlines()
        x0, y0, eps = float(lines[0]), float(lines[1]), float(lines[2])


print('\nГде вы хотите вывести результаты (найденный корень уравнения, значение функции в корне, число итераций)?')
print("1: в консоли\n2: в файле")
output_number = input()
while output_number not in {'1', '2'}:
    output_number = input('Выберите первый (1) или второй (2) вариант: ')

if output_number == '2':
    output_filename = input("Введите название файла: ").strip()


# --- Вычисление корней ---

X, x, Y, y, n = newtons_method(x0, y0, eps)
print_results(f"\nИтог: x = {X:.6f}, y = {Y:.6f}, шагов: {n}, вектор погрешностей: [{abs(X - x):.6f}, {abs(Y - y):.6f}]")
