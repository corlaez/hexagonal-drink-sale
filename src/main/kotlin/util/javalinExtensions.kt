package util;

import io.javalin.http.Context

private val charset = """<meta charset="UTF-8">"""
private val htmx = """<script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.10/dist/htmx.js" integrity="sha384-Q+Dky3iHVJOr6wUjQ4ulh6uQ76an/t+ak1+PjMVaxRjbZamFLAG+u9InkfjbsEQf" crossorigin="anonymous"></script>"""
private val picoCss = """<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@2.1.1/css/pico.classless.min.css">"""
private val styles = """<style>.grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; } @media (max-width: 768px) { .grid { grid-template-columns: 1fr; } }</style>"""
fun head(title: String) = "<head>$charset $htmx $picoCss <title>$title</title> $styles</head>"

fun Context.withNoCacheHeaders(): Context {
        header("Cache-Control", "no-cache, no-store, must-revalidate")
        header("Pragma", "no-cache")
        header("Expires", "0")
        return this
}
