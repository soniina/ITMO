import pygame
import os

WHITE = (255, 255, 255)
BLACK = (0, 0, 0)

x = 400
y = 100
os.environ['SDL_VIDEO_WINDOW_POS'] = "%d,%d" % (x,y)

pygame.init()

W = 700
H = 700

sc = pygame.display.set_mode((W, H))
pygame.display.set_caption("Множества Жюлиа")
sc.fill(WHITE)

clock = pygame.time.Clock()

c = complex(-0.5251993, 0.5251993)
# c = complex(-1)
# c = complex(0.377, -0.248)
# c = complex(-0.2, 0.75)
P = 350                     # размер [2*P+1 x 2*P+1]
scale = P / 0.025               # масштабный коэффициент
n_iter = 1000                # число итераций для проверки принадлежности к множеству Жюлиа
view = (0, 0)

for x in range(-P + view[0], P + view[0]):
    for y in range(-P + view[1], P + view[1]):
        a = x / scale
        b = y / scale
        z = complex(a, b)
        n = 0
        for n in range(n_iter):
            z = z**2 + c
            if abs(z) > 2:
                break

        if n == n_iter-1:
            r = g = b = 0
        else:
            r = (n % 2) * 32 + 128
            g = (n % 4) * 64
            b = (n % 2) * 16 + 128

        pygame.draw.circle(sc, (r, g, b), (x + P - view[0], y + P - view[1]), 1)

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            exit()

    pygame.display.update()
    clock.tick(30)
