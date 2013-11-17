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
            <p id="title" class="col-md-4"></p>

            <p id="status" class="text-middle small text-muted">Saved</p>
        </div>
        <div class="panel-body">
            <textarea id='editor' class="form-control" rows="20">
            </textarea>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="/_ah/channel/jsapi"></script>
<script src="lib/vanilla.js"></script>
<script src="js/channel.js"></script>
<script src="js/editor.js"></script>
<script src="js/transform.js"></script>
<script src="js/operations.js"></script>
<script src="js/view.js"></script>
<script>
    var view = initView();
    var collaborativeEditor = initEditor('${documentId}', '${clientId}', view);
    view.setPresenter(collaborativeEditor);
</script>
</html>