%define SYS_EXIT 60
%define SYS_WRITE 1
%define SYS_READ 0
%define STDOUT 1
%define STDIN 0

section .text

; Принимает код возврата и завершает текущий процесс
exit: 
    mov rax, SYS_EXIT
    syscall


; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    mov rax, rdi
  .counter:
    cmp byte[rdi], 0
    je .end
    inc rdi
    jmp .counter
  .end:
    sub rdi, rax
    mov rax, rdi
    ret


; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
    push rdi
    call string_length
    pop rsi
    mov  rdx, rax
    mov  rax, SYS_WRITE
    mov  rdi, STDOUT
    syscall
    ret


; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rdi, `\n`


; Принимает код символа и выводит его в stdout
print_char:
    sub rsp, 1
    mov byte[rsp], dil
    mov rax, SYS_WRITE
    mov rsi, rsp
    mov rdi, STDOUT
    mov rdx, 1
    syscall
    add rsp, 1
    ret


; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
    cmp rdi, 0
    jnl print_uint
    
    push rdi
    mov rdi, '-'
    call print_char
    pop rdi
    neg rdi


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    lea r8, [rsp - 1]
    sub rsp, 24
    mov r10, 10
    mov rax, rdi
    mov byte[r8], 0
    .next:
        dec r8
        xor rdx, rdx
        div r10 
        add rdx, '0'
        mov byte[r8], dl
        test rax, rax
        jnz .next
    mov rdi, r8
    call print_string
        
    add rsp, 24
    ret 


; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
    .next:
        mov al, byte[rdi]
        mov ah, byte[rsi]
        test al, al
        jz .first_end
        test ah, ah
        jz .false
        cmp al, ah
        jne .false
        add rdi, 1
        add rsi, 1
        jmp .next
    .first_end:
        test ah, ah
        jnz .false
    mov rax, 1
    jmp .end
    .false:
        xor rax, rax
    .end:
    ret


; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    sub rsp, 8
    mov rax, SYS_READ
    mov rdi, STDIN
    mov rsi, rsp
    mov rdx, 1
    syscall

    test rax, rax
    jz .end  

    mov al, byte[rsp]
    
    .end:
        add rsp, 8
        ret 


; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале.
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера.
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
read_word:
    xor rdx, rdx ; размер слова

    test rsi, rsi
    jz .fail

    dec rsi
    .skip:
        push rdi
        push rsi
        push rdx
        call read_char
        pop rdx
        pop rsi
        pop rdi
        cmp al, ' '
        je .skip
        cmp al, `\t`
        je .skip
        cmp al, `\n`
        je .skip

    .next:
        test al, al
        jz .success
        cmp al, ' '
        je .success
        cmp al, `\t`
        je .success
        cmp al, `\n`
        je .success

        cmp rdx, rsi
        jae .fail

        mov byte[rdi + rdx], al
        inc rdx

        push rdi
        push rsi
        push rdx
        call read_char
        pop rdx
        pop rsi
        pop rdi

        jmp .next

    .success:
        mov byte[rdi + rdx], 0
        mov rax, rdi
        ret

    .fail:
        xor rax, rax
        ret


; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
    xor r8, r8
    mov r10, 10
    xor rax, rax
    .read:
        movzx r9, byte[rdi + r8]
        cmp r9, '0'
        jb .end
        cmp r9, '9'
        ja .end

        sub r9, '0'
        mul r10
        add rax, r9
        inc r8
        jmp .read

    .end:
        mov rdx, r8
        ret


; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
    sub rsp, 8
    movzx r9, byte[rdi]
    cmp r9, '-'
    jne .positive

    .negative:
        inc rdi
        call parse_uint
        neg rax
        inc rdx
        jmp .end

    .positive:
        call parse_uint

    .end:
        add rsp, 8
        ret


; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
    mov r9, rdx
    .next:
        test r9, r9
        jle .check
        mov cl, byte[rdi]
        mov byte[rsi], cl
        inc rsi
        inc rdi
        dec r9
        jmp .next
    .check:
        cmp byte[rdi], '0'
        jne .fail

    sub rdx, r9
    mov rax, rdx
    ret

    .fail:
        xor rax, rax
        ret
                