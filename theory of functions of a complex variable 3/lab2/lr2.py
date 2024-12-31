import numpy as np
import matplotlib.pyplot as plt


def generate_M1_points(density, extent):
    """Генерация точек для области M1: Re(z) + Im(z) < -1."""
    x = np.linspace(-extent, extent, density)
    y = np.linspace(-extent, extent, density)
    X, Y = np.meshgrid(x, y)
    mask = X + Y < -1
    return X[mask], Y[mask]


def shift_z(x, y):
    """Сдвиг z -> z + 1."""
    return x + 1, y


def rotate_z(x, y, angle=-np.pi / 4):
    """Вращение на заданный угол."""
    z = x + 1j * y
    rotated_z = z * np.exp(1j * angle)
    return rotated_z.real, rotated_z.imag


def map_to_unit_circle(x, y):
    """Преобразование в единичный круг."""
    z = x + 1j * y
    mapped_z = (1 + z) / (1 - z)
    return mapped_z.real, mapped_z.imag


def scale_circle(x, y, scale=np.pi):
    """Масштабирование до радиуса pi."""
    return x * scale, y * scale


def plot_area(x, y, title, ax):
    """Визуализация области."""
    ax.fill(x, y, color='skyblue', alpha=0.5)
    ax.axhline(0, color='black', linewidth=0.5)
    ax.axvline(0, color='black', linewidth=0.5)
    ax.set_aspect('equal', adjustable='box')
    ax.set_title(title)
    ax.grid(True)
    ax.set_xlim([-np.pi - 1, np.pi + 1])
    ax.set_ylim([-np.pi - 1, np.pi + 1])


def main():
    density = 2000  # Плотность точек
    extent = 10   # Границы области

    # Этап 1: Множество M1
    x, y = generate_M1_points(density, extent)

    # Этап 2: Сдвиг
    x_shifted, y_shifted = shift_z(x, y)

    # Этап 3: Вращение
    x_rotated, y_rotated = rotate_z(x_shifted, y_shifted)

    # Этап 4: Преобразование в единичный круг
    x_circle, y_circle = map_to_unit_circle(x_rotated, y_rotated)

    # Этап 5: Масштабирование до радиуса pi
    x_final, y_final = scale_circle(x_circle, y_circle)

    # Визуализация
    fig, axs = plt.subplots(1, 5, figsize=(20, 4))

    # Исходная область M1
    plot_area(x, y, "M1: Re(z) + Im(z) < -1", axs[0])

    # После сдвига
    plot_area(x_shifted, y_shifted, "Сдвиг: z -> z + 1", axs[1])

    # После вращения
    plot_area(x_rotated, y_rotated, "Вращение: z -> z * e^(-i*pi/4)", axs[2])

    # После преобразования в круг
    plot_area(x_circle, y_circle, "Преобразование: w = (1+z)/(1-z)", axs[3])

    # Финальная область
    plot_area(x_final, y_final, "Круг радиуса π", axs[4])

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    main()
