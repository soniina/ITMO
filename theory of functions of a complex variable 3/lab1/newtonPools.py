import pygame
import os

WHITE = (255, 255, 255)

x = 400
y = 100
os.environ['SDL_VIDEO_WINDOW_POS'] = "%d,%d" % (x, y)

pygame.init()

W = 700
H = 700

sc = pygame.display.set_mode((W, H))
pygame.display.set_caption("Бассейны Ньютона")
sc.fill(WHITE)

clock = pygame.time.Clock()

max_value = 1e+6
min_value = 1e-6
P = 350
scale = 0.00000005  # масштабирование для преобразования координат
n_iter = 50
view = (-228000, -285000)

# Генерация бассейнов Ньютона
for x in range(-P + view[0], P + view[0]):
    for y in range(-P + view[1], P + view[1]):
        z_x = x * scale
        z_y = y * scale
        d_x, d_y = z_x, z_y  # d - разница между z и предыдущим z
        n = 0
        while (z_x**2 + z_y**2 < max_value) and (d_x**2 + d_y**2 > min_value) and (n < n_iter):
            t_x, t_y = z_x, z_y
            # Метод Ньютона для функции z^3 - 1
            p = (t_x**2 + t_y**2)**2
            z_x = (2/3) * t_x + (t_x**2 - t_y**2) / (3 * p)
            z_y = (2/3) * t_y * (1 - t_x / p)
            d_x = abs(t_x - z_x)
            d_y = abs(t_y - z_y)
            n += 1

        col = (n * 9) % 255
        pygame.draw.circle(sc, (col, 0, col), (x + P - view[0], y + P - view[1]), 1)

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            exit()

    pygame.display.update()
    clock.tick(30)
