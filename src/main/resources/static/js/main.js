$(function(){

    $("#login").click(function () {
        var data = {
            "usernameOrEmail": $("#userlogin").val(),
            "password": $("#passlogin").val()
        };

        data = JSON.stringify(data);
        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.addEventListener("readystatechange", function () {
            if (this.readyState === 4) {
                var resp = JSON.parse(this.responseText);
                console.log(resp);
                localStorage.setItem("token",resp.accessToken);
                if(resp.accessToken){
                    window.location = "details.html";
                }
            }
        });

        xhr.open("POST", "http://localhost:8082/api/auth/signin");
        xhr.setRequestHeader("content-type", "application/json");
        xhr.setRequestHeader("cache-control", "no-cache");
        xhr.setRequestHeader("postman-token", "442b181c-737e-04e9-f534-1d8d192b2523");

        xhr.send(data);
    })

    $("#register").click(function(){
        var data = JSON.stringify({
            "name": $("#namerg").val(),
            "username": $("#usernamerg").val(),
            "email": $("#emailrg").val(),
            "password": $("#passrg").val()
        });

        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;

        xhr.addEventListener("readystatechange", function () {
            if (this.readyState === 4) {
                console.log(this.responseText);
            }
        });

        xhr.open("POST", "http://localhost:8082/api/auth/signup");
        xhr.setRequestHeader("content-type", "application/json");
        xhr.setRequestHeader("cache-control", "no-cache");
        xhr.setRequestHeader("postman-token", "e96d9eb5-112b-34b9-1de6-c29c3d831da6");

        xhr.send(data);
    })
});