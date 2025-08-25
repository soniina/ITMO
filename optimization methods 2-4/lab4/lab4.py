MAX_ITERATIONS = 500


def newtons_method(x0, df, ddf, eps, n=0):
    """Метод Ньютона"""
    x1 = x0 - df(x0)/ddf(x0)
    if abs(df(x1)) <= eps:
        return x1
    return newtons_method(x1, df, ddf, eps, n + 1)


def coordinate_descent_method(x1, x2, eps, n = 0):
    """Метод покоординатного спуска"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")

    old_x1, old_x2 = x1, x2

    df1 = lambda x: 8*x - 3*old_x2 + 9          # ∂f/∂x1
    ddf1 = lambda x: 8                      # ∂²f/∂x1²
    x1 = newtons_method(old_x1, df1, ddf1, eps)

    df2 = lambda x: 10*x - 3*x1 - 2         # ∂f/∂x2
    ddf2 = lambda x: 10                     # ∂²f/∂x2²
    x2 = newtons_method(old_x2, df2, ddf2, eps)

    print(f"Итерация {n + 1}: x1 = {x1:.6f}, x2 = {x2:.6f}, f = {f(x1, x2):.6f}, Δ = {abs(f(x1, x2) - f(old_x1, old_x2)):.8f}")
    if abs(f(x1, x2) - f(old_x1, old_x2)) < eps:
        return x1, x2, n
    return coordinate_descent_method(x1, x2, eps, n + 1)


def gradient_descent_method(x1, x2, eps, lamb=0.25, n=0):
    """Метод градиентного спуска"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")

    old_x1, old_x2 = x1, x2
    x1 = old_x1 - lamb * grad_f[0](old_x1, old_x2)
    x2 = old_x2 - lamb * grad_f[1](old_x1, old_x2)

    print(f"Итерация {n + 1}: x1 = {x1:.6f}, x2 = {x2:.6f}, f = {f(x1, x2):.6f}, Δ = {abs(f(x1, x2) - f(old_x1, old_x2)):.8f}")

    if f(x1, x2) >= f(old_x1, old_x2):
        return gradient_descent_method(old_x1, old_x2, eps, lamb * 0.6, n)
    if abs(f(x1, x2) - f(old_x1, old_x2)) < eps:
        return x1, x2, n
    return gradient_descent_method(x1, x2, eps, lamb, n + 1)


def steepest_descent_method(x1, x2, eps, n=0):
    """Метод наискорейшего спуска"""
    if n > MAX_ITERATIONS:
        raise ValueError("Метод не сошёлся за максимальное число итераций.")

    g1, g2 = grad_f[0](x1, x2), grad_f[1](x1, x2)

    if (g1**2 + g2**2)**0.5 < eps:
        return x1, x2, n

    df = lambda h: (-8*g1 + 3*g2)*(x1 - h*g1) + (-10*g2 + 3*g1)*(x2 - h*g2) - 9*g1 + 2*g2
    ddf = lambda h: 8*g1**2 - 6*g1*g2 + 10*g2**2
    h = newtons_method(0, df, ddf, eps)

    if ddf(h) <= 0:
        raise ValueError("Найдена седловая точка или максимум. Попробуйте другую начальную точку.")

    x1 -= h*g1
    x2 -= h*g2

    print(f"Итерация {n + 1}: x1 = {x1:.6f}, x2 = {x2:.6f}, f = {f(x1, x2):.6f}, ‖∇f(x1,x2)‖ = {(grad_f[0](x1, x2)**2 + grad_f[1](x1, x2)**2)**0.5:.8f}")
    return steepest_descent_method(x1, x2, eps, n + 1)


print('Методы оптимизации. Лабораторная работа 4. Вариант 13.')

# --- Исходные данные ---

x1_0, x2_0 = 2, 3
eps = 0.0001
f = lambda x1, x2: 4*x1**2 + 5*x2**2 - 3*x1*x2 + 9*x1 - 2*x2 + 5
grad_f = (lambda x1, x2: 8*x1 - 3*x2 + 9, lambda x1, x2: 10*x2 - 3*x1 - 2)

# --- Выбор метода ---

print('\n1: Метод покоординатного спуска\n2: Метод градиентного спуска\n3: Метод наискорейшего спуска')
method_number = input('Выберите метод решения: ')
while method_number not in {'1', '2', '3'}:
    method_number = input('Выберите метод покоординатного спуска (1), метод градиентного спуска (2)'
                          ' и метод наискорейшего спуска (3): ')
print()


# --- Вычисление минимума ---

if method_number == '1':
    min_x1, min_x2, n = coordinate_descent_method(x1_0, x2_0, eps)
elif method_number == '2':
    min_x1, min_x2, n = gradient_descent_method(x1_0, x2_0, eps)
else:
    min_x1, min_x2, n = steepest_descent_method(x1_0, x2_0, eps)

print(f"\nМинимум {f(min_x1, min_x2):.6f} достигнут в точке: ({min_x1:.6f}, {min_x2:.6f}) за {n} итераций.")
