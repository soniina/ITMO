    .data

input_addr:      .word  0x80                     \ Адрес для ввода n
output_addr:     .word  0x84                     \ Адрес для вывода результата

overflow_error:  .word  0xCCCCCCCC
const_0:         .word  0                        \ Константа 0
const_1:         .word  1                        \ Константа 1
const_minus_1:   .word  -1                       \ Константа -1

    .text

_start:
    @p input_addr a! @
    fibonacci
    @p output_addr a! !
    halt

fibonacci:
    dup
    if ret

    dup
    inv
    -if invalid_n

    @p const_0 a!
    @p const_1

fibonacci_while:

    over
    @p const_minus_1 +

    dup
    if fibonacci_finish
    over

    a over
    dup a!
    +

    dup
    inv
    -if overflow

    fibonacci_while
    ;

fibonacci_finish:
    drop
ret:
    ;

overflow:
    drop
    drop
    @p overflow_error
    ;

invalid_n:
    drop
    @p const_minus_1
    ;