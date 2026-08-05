package util

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.Widget
import org.jline.terminal.TerminalBuilder
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import org.jline.utils.InfoCmp

object FancyConsole {
    private val terminal = TerminalBuilder.builder().system(true).build()
    private val reader = LineReaderBuilder.builder()
        .terminal(terminal)
        .variable(LineReader.AMBIGUOUS_BINDING, 0)
        .build().also {
            val abortOnEsc = Widget { throw UserInterruptException("ESC pressed") }
            it.keyMaps[LineReader.MAIN]?.bind(abortOnEsc, "\u001b")
        }

    fun readln(prompt: String): String {
        print(prompt)
        return reader.readLine()
    }
    fun <R> retryReadln(s: String, lambda: (String) -> R?): R {
        while (true) {
            val inputDeUsuario: String = readln(s)
            val inputTransformado: R? = lambda(inputDeUsuario)
            if (inputTransformado != null)
                return inputTransformado
        }
    }
    fun println(string: String) {
        kotlin.io.println(string)
    }
    fun clearScreen() {
        terminal.puts(InfoCmp.Capability.clear_screen)// Limpia el area visible del terminal
        terminal.writer().print("\u001b[3J")// Borra el historial que estaba escondido arriba
        terminal.flush()
    }
}

private fun conEstilo(attributedStyleConstant: Int): AttributedStringBuilder {
    return AttributedStringBuilder().style(
        AttributedStyle.DEFAULT.foreground(attributedStyleConstant).bold()
    )
}

fun String.enRojo() = conEstilo(AttributedStyle.RED).append(this).toAnsi()
fun String.enVerde(): String = conEstilo(AttributedStyle.GREEN).append(this).toAnsi()
fun String.enAzul(): String = conEstilo(AttributedStyle.BLUE).append(this).toAnsi()
