<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>Students</h1>
    <p><a href="/agregarEstudiante/">Agregar estudiante</a> </p>
    <table border="2px">
        <thead>
        <tr>
            <td>
                <b>Matricula</b>
            </td>
            <td>
                <b>Nombre</b>
            </td>
            <td>
                <b>Apellido</b>
            </td>
            <td>
                <b>Telefono</b>
            </td>
        </tr>
        </thead>
        <tbody>
            <#list students as student>
                <tr>
                    <td>${student.matricula}</td>
                    <td>${student.nombre}</td>
                    <td>${student.apellido}</td>
                    <td>${student.telefono}</td>
                    <td><a href="/estudiante/${student.matricula}">Ver</a></td>
                    <td><a href="/borrar/${student.matricula}">Borrar</a></td>
                    <td><a href="/editarEstudiante/${student.matricula}">Editar</a></td>
                </tr>
            </#list>
        </tbody>
    </table>
</body>
</html>