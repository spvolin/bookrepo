<!DOCTYPE html>

<html lang="en" ng-app="bookApp">
    <head>
        <title>${title}</title>
        <link href="css/bootstrap.css" rel="stylesheet"/>
        <link href="css/app.css" rel="stylesheet"/>
        <link href="css/main.css" rel="stylesheet"/>
<!--        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous"> -->
<!--        <link rel="stylesheet" href="css/main.css"> -->        
    </head>
    <body>

        <div ng-view></div>

        <script src="js/lib/angular.min.js" ></script>
        <script src="js/lib/angular-ui-router.min.js" ></script>
<!--        <script src="js/lib/angular-route.js" ></script> -->
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular-route.min.js"></script>
        <script src="js/lib/localforage.min.js" ></script>
        <script src="js/lib/ngStorage.min.js"></script>
        <script src="js/app/v2/app.js"></script>
        <script src="js/app/v2/auth.service.js"></script>
        <script src="js/app/v2/auth.index.controller.js"></script>
        <script src="js/app/main.js"></script> 
        <script src="js/app/BookService.js"></script> 
        <script src="js/app/BookController.js"></script>
<!--        <script src="js/app/FileService.js"></script>  -->
<!--        <script src="js/app/FileController.js"></script>  -->
    </body>
</html>