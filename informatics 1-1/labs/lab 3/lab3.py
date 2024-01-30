import re

print('Задание ', end='')
task = int(input())

if task == 1:
    #смайлик [<}/

    pattern1 = r'\[<}/'
    test1 = '[<}/[<}/[<}/'      #Ответ: 3
    test2 = '[>}/'              #Ответ: 0
    test3 = '[[<}/////'         #Ответ: 1
    test4 = '[<<}/'             #Ответ: 0
    test5 = '[<}[<}//[<}/[<}'   #Ответ: 2

    print('test1: ', len(re.findall(pattern1, test1)))
    print('test2: ', len(re.findall(pattern1, test2)))
    print('test3: ', len(re.findall(pattern1, test3)))
    print('test4: ', len(re.findall(pattern1, test4)))
    print('test5: ', len(re.findall(pattern1, test5)))


elif task == 2:
    #409295 % 6 = 5

    pattern2 = r'\w*[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]{2}\w*(?=\s+\b[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ](?:[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ\s]?){,2}[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*\b|\s+\b[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]+(?:[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ\s]?){,3}[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*\b)'
    check_next = r'\s+\b[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ](?:[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ\s]?){,2}[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*\b|\s+\b[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]+(?:[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*[^ёуеыаоэяиюЁУЕЫАОЭЯИЮ\s]?){,3}[ёуеыаоэяиюЁУЕЫАОЭЯИЮ]*\b'

    test1 = 'Кривошеее существо гуляет по парку'        #Ответ: гуляет
    test2 = 'Кривошеее существо гуляет попп парку'      #Ответ: гуляет
    test3 = 'Кривошеее существо гуляет попппп парку'    #Ответ: -
    test4 = 'ааауляет по парку'                         #Ответ: ааауляет
    test5 = ' ааауляет    опопо парку'                  #Ответ: ааауляет
    test6 = 'еесть кашуу кашууу ееесть'                 #Ответ: еесть кашуу кашууу

    print('test1: ', *re.findall(pattern2, test1))
    print('test2: ', *re.findall(pattern2, test2))
    print('test3: ', *re.findall(pattern2, test3))
    print('test4: ', *re.findall(pattern2, test4))
    print('test5: ', *re.findall(pattern2, test5))
    print('test6: ', *re.findall(pattern2, test6))


elif task == 3:
    #409295 % 5 = 0

    pattern3 = r'\b[\w.]+@([a-zA-Z.]+\.[a-z]+)\b'
    test1 = 'students.spam@yandex.ru'                   #Ответ: yandex.ru
    test2 = 'example@example'                           #Ответ: Fail!
    test3 = 'example@example.com'                       #Ответ: example.com
    test4 = 'spam_spam_spam.spam@mail.ru'               #Ответ: mail.ru
    test5 = 'email@.com'                                #Ответ: Fail!
    test6 = 'email_email@ya.ya.ya.ru'                   #Ответ: ya.ya.ya.ru
    test7 = 'email@not_yandex.ru'                       #Ответ: Fail!
    test8 = 'EMAIL.@gmail.com'                          #Ответ: gmail.com

    anss = []
    anss.append(re.findall(pattern3, test1))
    anss.append(re.findall(pattern3, test2))
    anss.append(re.findall(pattern3, test3))
    anss.append(re.findall(pattern3, test4))
    anss.append(re.findall(pattern3, test5))
    anss.append(re.findall(pattern3, test6))
    anss.append(re.findall(pattern3, test7))
    anss.append(re.findall(pattern3, test8))

    for i in range(len(anss)):
        if len(anss[i]) == 0: print(f'test{i + 1}: Fail!')
        else: print(f'test{i + 1}:', *anss[i])
