<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Collaborative text editor</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <p id="title" class="col-md-4">Please, select any document or create new one:</p>

            <p id="status" class="text-right small text-muted">Hello, ${userEmail}</p>
        </div>
        <div class="panel-body">
            <ul class="list-group">
                <c:forEach var="document" items="${documents}">
                    <li class="list-group-item">
                        <a href="/editor?documentId=${document.id}">${document.title}</a>
                    </li>
                </c:forEach>
            </ul>

            <form class="form-inline" role="form" method="POST" accept-charset="utf-8">
                <div class="form-group">
                    <input name="title" class="form-control" placeholder="Title">
                </div>
                <button type="submit" class="btn btn-primary">Create!</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>