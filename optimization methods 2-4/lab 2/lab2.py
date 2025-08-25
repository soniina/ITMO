import math

MAX_ITERATIONS = 500


def half_division_method(a, b, eps, n=0):
    """Метод половинного деления"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    x1 = (a + b - eps) / 2
    x2 = (a + b + eps) / 2
    y1 = f(x1)
    y2 = f(x2)
    print(f"Шаг {n}: [a, b] = [{a:.6f}, {b:.6f}], x1 = {x1:.6f}, x2 = {x2:.6f}, y1 = {y1:.6f}, y2 = {y2:.6f}, b - a = {b - a:.6f}")
    if b - a <= 2*eps:
        return (a + b) / 2, f((a + b) / 2), n
    if y1 > y2:
        return half_division_method(x1, b, eps, n + 1)
    else:
        return half_division_method(a, x2, eps, n + 1)


def golden_section_method(a, b, x1, x2, eps, n=0):
    """Метод золотого сечения"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    print(f"Шаг {n}: [a, b] = [{a:.6f}, {b:.6f}], x1 = {x1:.6f}, x2 = {x2:.6f}, f(x1) = {f(x1):.6f}, "
          f"f(x2) = {f(x2):.6f}, |b - a| = {abs(b - a):.6f}")
    if abs(b - a) <= eps:
        return (a + b) / 2, f((a + b) / 2), n
    if f(x1) < f(x2):
        return golden_section_method(a, x2, a + 0.382 * (x2 - a), x1, eps, n + 1)
    else:
        return golden_section_method(x1, b, x2, a + 0.618 * (b - x1), eps, n + 1)


def chord_method(a, b, eps, n=0):
    """Метод хорд"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")
    tx = a - df(a)/(df(a) - df(b)) * (a - b)
    print(f"Шаг {n}: [a, b] = [{a:.6f}, {b:.6f}], x̃ = {tx:.6f}, f'(x̃) = {df(tx):.6f}")
    if abs(df(tx)) <= eps:
        return tx, f(tx), n
    if df(tx) > 0:
        return chord_method(a, tx, eps, n + 1)
    else:
        return chord_method(tx, b, eps, n + 1)


def newtons_method(x0, eps, n=0):
    """Метод Ньютона"""
    x1 = x0 - df(x0)/ddf(x0)
    print(f"Шаг {n}: x_k = {x0:.6f}, x_(k+1) = {x1:.6f}, f'(x_(k+1)) = {df(x1):.6f}, |f'(x_(k+1))| = {abs(df(x1)):.6f}")
    if abs(df(x1)) <= eps:
        return x1, f(x1), n
    return newtons_method(x1, eps, n + 1)


print('Методы оптимизации. Лабораторная работа 2. Вариант 13.')

# --- Исходные данные ---

a = 0.5
b = 1.5
eps = 0.0001
f = lambda x: 1/x + math.e**x
df = lambda x: -1/x**2 + math.e**x
ddf = lambda x : 2/x**3 + math.e**x


# --- Выбор метода ---

print('\n1: Метод половинного деления\n2: Метод золотого сечения\n3: Метод хорд\n4: Метод Ньютона')
method_number = input('Выберите метод решения: ')
while method_number not in {'1', '2', '3', '4'}:
    method_number = input('Выберите метод половинного деления (1), метод золотого сечения (2), метод хорд (3)'
                          ' и метод Ньютона (4): ')
print()


# --- Вычисление минимума ---

if method_number == '1':
    x, y, n = half_division_method(a, b, eps)
elif method_number == '2':
    x, y, n = golden_section_method(a, b, a + 0.382 * (b - a), a + 0.618 * (b - a), eps)
elif method_number == '3':
    x, y, n = chord_method(a, b, eps)
else:
    x, y, n = newtons_method((a + b) / 2, eps)

print(f"\nИтог: x = {x:.6f}, f(x) = {y:.6f}, шагов: {n}")
