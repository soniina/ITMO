import math


def left_rectangle_method(I0, n):
    h = (b - a) / n
    I1 = h * sum(f(a + i*h) for i in range(0, n))
    print(f"Число разбиений интервала n = {n}: h = {h:.6f}, I_0 = {I0:.6f}, I_1 = {I1:.6f}, |I_1 - I_0| = {abs(I1 - I0):.15f}")

    if abs(I1 - I0) < eps:
        return I1, n
    return left_rectangle_method(I1, n * 2)


def medium_rectangle_method(I0, n):
    h = (b - a) / n
    I1 = h * sum((f(a + i*h) + f(a + (i-1)*h)) / 2 for i in range(1, n + 1))
    print(f"Число разбиений интервала n = {n}: h = {h:.6f}, I_h = {I0:.6f}, I_h/2 = {I1:.6f}, |I_h/2 - I_h| = {abs(I1 - I0):.15f}")

    if abs(I1 - I0)/3 < eps:
        return I1, n
    return right_rectangle_method(I1, n * 2)


def right_rectangle_method(I0, n):
    h = (b - a) / n
    I1 = h * sum(f(a + i*h) for i in range(1, n + 1))
    print(f"Число разбиений интервала n = {n}: h = {h:.6f}, I_h = {I0:.6f}, I_h/2 = {I1:.6f}, |I_h/2 - I_h| = {abs(I1 - I0):.15f}")

    if abs(I1 - I0) < eps:
        return I1, n
    return right_rectangle_method(I1, n * 2)


def trapezoid_method(I0, n):
    h = (b - a) / n
    I1 = h * ((f(a) + f(b)) / 2 + sum(f(a + i*h) for i in range(1, n)))
    print(f"Число разбиений интервала n = {n}: h = {h:.6f}, I_h = {I0:.6f}, I_h/2 = {I1:.6f}, |I_h/2 - I_h| = {abs(I1 - I0):.15f}")

    if abs(I1 - I0)/3 < eps:
        return I1, n
    return trapezoid_method(I1, n * 2)


def Simpson_method(I0, n):
    h = (b - a) / n
    I1 = h/3 * (f(a) + 4*sum(f(a + i*h) for i in range(1, n, 2)) + 2*sum(f(a + i*h) for i in range(2, n - 1, 2)) + f(b))
    print(f"Число разбиений интервала n = {n}: h = {h:.6f}, I_h = {I0:.6f}, I_h/2 = {I1:.6f}, |I_h/2 - I_h| = {abs(I1 - I0):.15f}")

    if abs(I1 - I0)/15 < eps:
        return I1, n
    return Simpson_method(I1, n * 2)


print('Вычислительная математика. Лабораторная работа 3: "Численное интегрирование". Вариант 13\n')

# --- Исходные данные ---

n = 4


# --- Функции и их интегралы ---

equations = {
    '1': lambda x: x**3 - 3*x**2 + 6*x - 28,
    '2': lambda x: math.sin(x) + 2*x,
    '3': lambda x: math.e**2 + 2/3*x,
    '4': lambda x: math.cos(4*x)
}

integrals = {
    '1': lambda x: x**4/4 - x**3 + 3*x**2 - 28*x,
    '2': lambda x: -math.cos(x) + x**2,
    '3': lambda x: math.e**2*x + 1/3*x**2,
    '4': lambda x: math.sin(4*x)/4
}


# --- Ввод данных ---

print('1: x^3 - 3x^2 + 6x - 28\n2: sin(x) + 2x\n3: e^2 + 2/3x\n4: cos(4x)')
equation_number = input('Выберите уравнение: ')

while equation_number not in {'1', '2', '3', '4'}:
    equation_number = input('Выберите первое (1), второе (2), третье (3) или четвёртое (4) уравнение: ')

f = equations[equation_number]


print('\n1: Метод левых прямоугольников\n2: Метод средних прямоугольников\n3: Метод правых прямоугольников')
print('4: Метод трапеций\n5: Метод Симпсона')
method_number = input('Выберите метод решения: ')
while method_number not in {'1', '2', '3', '4', '5'}:
    method_number = input('Выберите метод левых прямоугольников (1), метод средних прямоугольников (2), '
                          'метод правых прямоугольников (3), '
                          'метод трапеций (4) или метод Симпсона (5): ')


print("\nВведите пределы интегрирования:")
a = float(input('a = '))
b = float(input('b = '))
while a >= b:
    print('Правая границы должна быть больше левой. Пожалуйста, повторите ввод.')
    b = float(input('b = '))
print("Введите точность вычисления:")
eps = float(input('ε = '))
print()

I = integrals[equation_number](b) - integrals[equation_number](a)


# --- Интегрирование ---

h = (b - a) / n
if method_number == '1':
    I_v, n = left_rectangle_method(h * sum(f(a + i*h) for i in range(0, n)), n * 2)
elif method_number == '2':
    I_v, n = medium_rectangle_method(h * sum((f(a + i*h) + f(a + (i-1)*h)) / 2 for i in range(1, n + 1)), n * 2)
elif method_number == '3':
    I_v, n = right_rectangle_method(h * sum(f(a + i*h) for i in range(1, n + 1)), n * 2)
elif method_number == '4':
    I_v, n = trapezoid_method(h * ((f(a) + f(b)) / 2 + sum(f(a + i*h) for i in range(1, n))), n * 2)
elif method_number == '5':
    I_v, n = Simpson_method(h/3 * (f(a) + 4*sum(f(a + i*h) for i in range(1, n, 2)) + 2*sum(f(a + i*h) for i in range(2, n - 1, 2)) + f(b)), n * 2)

print(f"\nИтог: I_точное = {I:.6f}, I_вычисленное = {I_v:.6f}, n = {n}, |I_точное - I_вычисленное| = {abs(I - I_v):.15f}")
