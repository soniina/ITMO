package handlers

data class ValidationResult(val isValid: Boolean = false, val message: String)

class Validator {
    fun validate(x: String, y: String, r: String): ValidationResult {
        try {
            if (isInt(x)) return ValidationResult(message = "Значение X должно иметь целочисленный тип данных")
            if (isInt(r)) return ValidationResult(message = "Значение R должно иметь целочисленный тип данных")
            if (x.toBigDecimal() !in (-3).toBigDecimal()..(5).toBigDecimal()) return ValidationResult(message = "Значение X должно быть в диапазоне от -3 до 5")
            if (y.toBigDecimal() !in (-5).toBigDecimal()..(3).toBigDecimal()) return ValidationResult(message = "Значение Y должно быть в диапазоне от -5 до 3")
            if (r.toBigDecimal() !in (1).toBigDecimal()..(5).toBigDecimal()) return ValidationResult(message = "Значение R должно быть в диапазоне от 1 до 5.")
            return ValidationResult(true, "Точка успешно проверена")
        } catch (e : NumberFormatException) {
            return ValidationResult(message = "Все параметры должны быть числовыми значениями.")
        }
    }

    private fun isInt(x: String) = x.toDouble() != x.toInt().toDouble()
}