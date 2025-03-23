import numpy as np
import sympy as sp
import matplotlib.pyplot as plt

MAX_ITERATIONS = 500


def print_results(result):
    """Вывод результатов в консоль или файл"""
    if output_number == '2':
        with open(output_filename, 'a') as file:
            file.write(result + '\n')
    else:
        print(result)


def build_graph():
    """Построение графика функции"""
    x_values = np.linspace(a, b, 400)
    y_values = f(x_values)

    plt.figure(figsize=(12, 10))

    plt.plot(x_values, y_values, label=f'f(x)')

    plt.xticks(np.arange(a, b, 0.25))

    plt.axhline(0, color='black', linewidth=1)
    plt.axvline(0, color='black', linewidth=1)
    plt.axvline(a, color='green', linestyle='--', label=f'a = {a:.3f}')
    plt.axvline(b, color='blue', linestyle='--', label=f'b = {b:.3f}')
    plt.title(f'График функции f(x) на интервале [{a}, {b}]')
    plt.xlabel('x')
    plt.ylabel('f(x)')
    plt.ylim(max(f(a), -25), min(f(b), 25))
    plt.legend()
    plt.grid(True)
    plt.show()


def is_monotonic_derivative():
    x = sp.symbols('x')

    test_points = [a + (b - a) * i / 10 for i in range(11)]
    derivative_values = [derivatives[equation_number].subs(x, point).evalf() for point in test_points]

    sign_changes = 0
    for i in range(1, len(derivative_values)):
        if derivative_values[i - 1] * derivative_values[i] < 0:
            sign_changes += 1

    return sign_changes == 0


def half_division_method(a, b, eps, n=0):
    """Метод половинного деления"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    x = (a + b) / 2
    print_results(f"Шаг {n}: x = {x:.6f}, f(x) = {f(x):.6f}, [a, b] = [{a:.6f}, {b:.6f}], |b - a| = {abs(b - a):.6f}")
    if abs(b - a) < eps and abs(f(x)) < eps:
        return (a + b) / 2, f((a + b) / 2), n
    if f(a) * f(x) > 0:
        return half_division_method(x, b, eps, n + 1)
    else:
        return half_division_method(a, x, eps, n + 1)


def secant_method(x0, x1, eps, n = 0):
    """Метод секущих"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    x = x1 - (x1-x0)/(f(x1)-f(x0)) * f(x1)  # используем разностное приближение
    print_results(f"Шаг {n}: x_(i-1) = {x0:.6f}, x_i = {x1:.6f}, x_(i+1) = {x:.6f}, f(x_(i+1)) = {f(x):.6f}, |x_(i+1) - x_i| = {abs(x1 - x0):.6f}")
    if abs(x - x1) <= eps or abs(f(x)) <= eps:
        return x, f(x), n
    return secant_method(x1, x, eps, n + 1)


def simple_iteration_method(x0, phi, eps, n = 0):
    """Метод простой итерации"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    x = phi(x0)
    print_results(f"Шаг {n}: x_i = {x0:.6f}, x_(i+1) = {x:.6f}, 𝝋(x_(i+1)) = {phi(x):.6f}, f(x_(i+1)) = {f(x):.6f}, |x_(i+1) - x_i| = {abs(x - x0):.6f}")
    if abs(f(x)) < eps:
        return x, f(x), n
    return simple_iteration_method(x, phi, eps, n + 1)


print('Вычислительная математика. Лабораторная работа 2_1: "Численное решение нелинейных уравнений". Вариант 13\n')


# --- Исходные данные ---

a = 0
b = 0
eps = 0


# --- Уравнения и их производные ---

x = sp.symbols('x')
equations = {
    '1': sp.sympify(2.74 * x**3 - 1.93 * x**2 - 15.28 * x - 3.72),
    '2': sp.sympify(3.12*sp.exp(0.8*x) - 2.45*sp.sin(1.3*x) + 4.67*x - 7.89),
    '3': sp.sympify(-0.38*x**3 - 3.42*x**2 + 2.51*x + 8.75),
    '4': sp.sympify(-2.71*sp.log(1 + 0.9*x) - 5.32*x + 4.12)
}

derivatives = {key:  sp.diff(equations[key]) for key in equations.keys()}
second_derivatives = {key: sp.lambdify(x, sp.diff(derivatives[key]), "numpy") for key in equations.keys()}


# --- Ввод данных ---

print('1: 2.74x^3 - 1.93x^2 - 15.28x - 3.72\n2: 3.12e^(0.8x) - 2.45sin(1.3x) + 4.67x - 7.89')
print('3: -0.38x^3 - 3.42x^2 + 2.51x + 8.75\n4: -2.71ln(1 + 0.9x) - 5.32x + 4.12')
equation_number = input('Выберите уравнение: ')

while equation_number not in {'1', '2', '3', '4'}:
    equation_number = input('Выберите первое (1), второе (2), третье (3) или четвёртое (4) уравнение: ')

f = sp.lambdify(x, equations[equation_number], "numpy")
df = sp.lambdify(x, derivatives[equation_number], "numpy")


print('\n1: Метод половинного деления\n2: Метод секущих\n3: Метод простой итерации')
method_number = input('Выберите метод решения: ')
while method_number not in {'1', '2', '3'}:
    method_number = input('Выберите метод половинного деления (1), метод секущих (2) или метод простой итерации (3): ')


print('\nКак вы хотите ввести исходные данные(границы интервала, начальное приближение к корню и погрешность вычисления)?')
print("1: с консоли\n2: с файла")
input_number = input()
while input_number not in {'1', '2'}:
    input_number = input('Выберите первый (1) или второй (2) вариант: ')

if input_number == '1':
    print("\nВведите границы интервала:")
    a = float(input('a = '))
    if equation_number == '4':
        while a <= -1/0.9:
            print('Выходит за области определения (x > -1/0.9). Пожалуйста, повторите ввод.')
            a = float(input('a = '))
    b = float(input('b = '))
    while a >= b:
        print('Правая границы должна быть больше левой. Пожалуйста, повторите ввод.')
        b = float(input('b = '))
    print("Введите погрешность вычисления:")
    eps = float(input('ε = '))
else:
    input_filename = input("Введите название файла: ").strip()
    with open(input_filename, 'r') as file:
        lines = file.readlines()
        a, b, eps = float(lines[0]), float(lines[1]), float(lines[2])
        if a >= b:
            raise ValueError('Правая границы должна быть больше левой.')
        if equation_number == '4':
            if a <= -1/0.9:
                raise ValueError('Левая граница выходит за области определения (x > -1/0.9).')

print('\nГде вы хотите вывести результаты (найденный корень уравнения, значение функции в корне, число итераций)?')
print("1: в консоли\n2: в файле")
output_number = input()
while output_number not in {'1', '2'}:
    output_number = input('Выберите первый (1) или второй (2) вариант: ')

if output_number == '2':
    output_filename = input("Введите название файла: ").strip()

# --- Построение графика функции ---

build_graph()


# --- Проверка существования единственного корня на заданном интервале ---

if f(a) * f(b) >= 0:
    raise ValueError("На заданном интервале отсутствуют корни!")
elif not is_monotonic_derivative():
    raise ValueError("На заданном интервале несколько корней!")


# --- Вычисление корней ---

print()

if method_number == '1':
    x, fx, n = half_division_method(a, b, eps)
    print_results(f"\nИтог: x = {x:.6f}, f(x) = {fx:.6f}, шагов: {n}")

elif method_number == '2':
    x0 = b
    if f(a) * second_derivatives[equation_number](a) > 0:
        x0 = a
    x, fx, n = secant_method(x0, x0 + eps, eps)
    print_results(f"\nИтог: x = {x:.6f}, f(x) = {fx:.6f}, шагов: {n}")

else:
    lamb = 1 / max(df(a), df(b))
    if df(a) > 0 and df(b) > 0:
        lamb = -lamb
    phi = lambda x: x + lamb * f(x)
    dphi = lambda x: 1 + lamb * df(x)
    q = max(abs(phi(a)), abs(phi(b)))
    print_results(f'|𝝋(a)| = {abs(phi(a))}, |𝝋(b)| = {abs(phi(b))}')
    if q < 1:
        print_results("Итерационная последовательность сходится!\n")
        if q <= 0.5:
            x, fx, n = simple_iteration_method(a, phi, eps)
        else:
            x, fx, n = simple_iteration_method(a, phi, (1 - q) / q * eps)
        print_results(f"\nИтог: x = {x:.6f}, f(x) = {fx:.6f}, шагов: {n}")
    else:
        print_results("Условие сходимости не выполняется!")
