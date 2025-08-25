    .data

buf:             .byte  '________________________________'    ; Буфер для хранения сообщения
name:            .byte  '_______________________'             ; Буфер для хранения имени
greet:           .byte  'Hello, \0'                           ; Приветственная часть сообщения
overflow_error:  .word   0xCCCCCCCC                           ; Ошибка переполнения
prompt:          .byte  'What is your name?\n\0'                ; Приглашение для ввода

input_addr:      .word  0x80                     ; Адрес для ввода имени
output_addr:     .word  0x84                     ; Адрес для вывода сообщения

ptr:             .word  0                        ; Указатель на данные
src_ptr:         .word  0                        ; Второй указатель на данные источника
char:            .word  0                        ; Временное хранение символа

max_name_length: .word  22                       ; Максимальная длина имени
name_length:     .word  0                        ; Текущая длина слова
const_1:         .word  1                        ; Константа 1
const_FF:        .word  0xFF                     ; Константа FF

clear_low_byte:  .word  0xFFFFFF00               ; Маска очистки младшего байта

    .data
    .org 136
null:            .word  '\0'                     ; Нулевой символ
exclamation:     .word  '!'                      ; Восклицательный знак
newline:         .word  '\n'                     ; Перевод строки


    .text

_start:

    ; Вывод приглашения
    load_imm     prompt
    store        ptr

print_prompt:

    load_ind     ptr
    and          const_FF

    xor          null
    beqz         print_prompt_end
    xor          null

    store_ind    output_addr

    load         ptr
    add          const_1
    store        ptr

    jmp          print_prompt

print_prompt_end:

    ; Проверка длины имени
    load_imm     name
    store        ptr

check_name_length:

    load_ind     input_addr
    and          const_FF
    xor          newline
    beqz         check_name_length_end

    xor          newline
    store        char
    load_ind     ptr
    and          clear_low_byte
    or           char
    store_ind    ptr

    load         name_length
    add          const_1
    store        name_length

    load         ptr
    add          const_1
    store        ptr

    load         max_name_length
    sub          name_length

    ble          too_long_name

    jmp          check_name_length

check_name_length_end:

    load         null
    store        char
    load_ind     ptr
    and          clear_low_byte
    or           char
    store_ind    ptr

    ; Формирование сообщения в буфере
    load_imm     buf
    store        ptr

    load_imm     greet
    store        src_ptr

write_greet_to_buf:

    load_ind     src_ptr
    and          const_FF

    xor          null
    beqz         write_greet_to_buf_end

    xor          null
    store_ind    ptr

    load         ptr
    add          const_1
    store        ptr

    load         src_ptr
    add          const_1
    store        src_ptr

    jmp          write_greet_to_buf

write_greet_to_buf_end:

    ; Копирование имени в буфер сообщения
    load_imm     name
    store        src_ptr

write_name_to_buf:

    load_ind     src_ptr
    and          const_FF

    xor          null
    beqz         write_name_to_buf_end
    xor          null

    store        char
    load_ind     ptr
    and          clear_low_byte
    or           char
    store_ind    ptr

    load         ptr
    add          const_1
    store        ptr

    load         src_ptr
    add          const_1
    store        src_ptr

    jmp          write_name_to_buf

write_name_to_buf_end:

    ; Добавление '!' и нуль-терминатора
    load         exclamation
    store        char
    load_ind     ptr
    and          clear_low_byte
    or           char
    store_ind    ptr

    load         ptr
    add          const_1
    store        ptr

    load         null
    store        char
    load_ind     ptr
    and          clear_low_byte
    or           char
    store_ind    ptr

    ; Вывод итогового сообщения
    load_imm     buf
    store        ptr

print_message:

    load_ind     ptr
    and          const_FF

    xor          null
    beqz         end

    xor          null
    and          const_FF
    store_ind    output_addr

    load         ptr
    add          const_1
    store        ptr

    jmp          print_message

end:

    halt


; Обработка недопустимо длинного имени
too_long_name:

    load         overflow_error
    store_ind    output_addr

    halt
