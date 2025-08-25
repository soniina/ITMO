import math

MAX_ITERATIONS = 500


def find_xs(x1):
    x2 = x1 + dx
    x3 = x1 - dx
    if f(x1) > f(x2):
        x3 = x1 + 2*dx
    return x1, x2, x3


def quadratic_approximation_method(x1, x2, x3, eps, n=0):
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")

    f1 = f(x1)
    f2 = f(x2)
    f3 = f(x3)

    Fmin = min(f1, f2, f3)
    xmin = x1 if Fmin == f1 else x2 if Fmin == f2 else x3
    denominator = (x2-x3) * f1 + (x3-x1) * f2 + (x1-x2) * f3
    if denominator == 0:
        print(f"Шаг {n}: x1 = {x1:.6f}, x2 = {x2:.6f}, x3 = {x3:.6f}, знаменатель в формуле для x̅ равен 0 => x1 = xmin ")
        return quadratic_approximation_method(*find_xs(xmin), eps, n + 1)

    x = 1/2 * ((x2**2-x3**2) * f1 + (x3**2-x1**2) * f2 + (x1**2-x2**2) * f3) / denominator
    fx = f(x)
    print(f"Шаг {n}: x1 = {x1:.6f}, x2 = {x2:.6f}, x3 = {x3:.6f}, x_min = {xmin:.6f},  ̅x ={x:.6f}, f(̅x) ={fx:.6f}, "
          f"|(Fmin - f(̅x))/f(̅x)|) ={abs((Fmin-fx)/fx):.6f}, |(xmin - ̅x)/̅x| ={abs((xmin-x)/x):.6f}")
    if abs((Fmin-fx)/fx) < eps and abs((xmin-x)/x) < eps:
        return x, fx, n
    else:
        if min(x1, x3) <= x <= max(x1, x3):
            x2 = min(xmin, x)
            if x1 > x3:
                x1 = x3
            x3 = max(xmin, x)
            return quadratic_approximation_method(x1, x2, x3, eps, n + 1)
        else:
            return quadratic_approximation_method(*find_xs(x), eps, n + 1)


print('Методы оптимизации. Лабораторная работа 3. Метод квадратичной аппроксимации. Вариант 13.\n')

# --- Исходные данные ---

a = 0.5
b = 1.5
eps = 0.0001
f = lambda x: 1/x + math.e**x


# --- Вычисление минимума ---

dx = (b - a) / 3
x, y, n = quadratic_approximation_method(*find_xs(a), eps)

print(f"\nИтог: x = {x:.6f}, f(x) = {y:.6f}, шагов: {n}")