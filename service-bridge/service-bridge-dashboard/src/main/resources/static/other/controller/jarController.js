app.controller('jarController', function ($scope, $controller, $http) {

    $controller('baseController', {$scope: $scope});

    $scope.searchJarInfoNameLike = "";

    $scope.loadJarName = "";

    // page：第几页；row：每页几行
    $scope.getJarInfos = function (page, row) {
        $http({
            method: "POST",
            url: "/jar/getJarInfos?nameLike=" + $scope.searchJarInfoNameLike + "&page=" + page + "&row=" + row,
        }).then(function successCallback(response) {
            console.log(response);
            $scope.jarInfosPagination.totalItems = response.data.total;//更新总记录数
            $scope.jarInfos = response.data.rows;
        }, function errorCallback(response) {

        });
    };


    $scope.jarInfosPagination = {
        currentPage: 1,
        itemsPerPage: 8,
        totalItems: 8000,
        pagesLength: 5,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.getJarInfos($scope.jarInfosPagination.currentPage, $scope.jarInfosPagination.itemsPerPage);
        }
    };

    $scope.recordLardJarInfo = function (jarName) {
        $scope.loadJarName = jarName;
    };

    $scope.clearInfo = function () {
        setInnerHtmlValue("loadJarInfo", "");
    };


    $scope.realLoadJar = function () {

        var spsid = document.getElementById("loadJarSpsid").value;

        $http({
            method: "POST",
            url: "/api/load-jar/" + spsid + "/" + $scope.loadJarName,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                setInnerHtmlValue("loadJarInfo", "加载成功！");
                return;
            }
            setInnerHtmlValue("loadJarInfo", "加载失败！");
        }, function errorCallback(response) {

        });

    };

    $scope.refreshCurrent = function () {
        $scope.getJarInfos($scope.jarInfosPagination.currentPage, $scope.jarInfosPagination.itemsPerPage);
    }

    $scope.getAllSpsid = function () {
        $http({
            method: "POST",
            url: "/jar/getAllSpsid",
        }).then(function successCallback(response) {
            $scope.allSpsid = response.data.rows;
            $("#loadJarSpsid").selectpicker("refresh");
        }, function errorCallback(response) {

        });
    };


    $scope.searchJarInfos = function () {

        var searchJarInfoNameLike = document.getElementById("searchJarInfoNameLike");

        var searchJarInfoNameLikeValue = searchJarInfoNameLike.value;

        $scope.searchJarInfoNameLike = searchJarInfoNameLikeValue;

        $scope.getJarInfos($scope.jarInfosPagination.currentPage, $scope.jarInfosPagination.itemsPerPage);
    };

    $scope.clearJarUploadInfo = function () {
        setInnerHtmlValue("jarUploadInfo", "");
        document.getElementById('jarName').value = "...";
    };

    $scope.uninstallJar = function (spsid, jarName) {

        $http({
            method: "POST",
            url: "/api/release-jar/" + spsid + "/" + jarName,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                alert("卸载成功！");
                $scope.refreshCurrent();
                return;
            }
            alert("卸载失败！");
        }, function errorCallback(response) {

        });

    };


});

function setJarName() {
    var jar = document.getElementById('jar');
    var fileName = jar.value;

    var i = fileName.lastIndexOf("fakepath");

    fileName = fileName.substring(i + 9);

    if (!/\.(jar)$/.test(fileName)) {
        setInnerHtmlValue("jarUploadInfo", "选择正确的jar文件")
        fileName = "...";
    }
    document.getElementById('jarName').value = fileName;
}

function setInnerHtmlValue(id, msg) {
    document.getElementById(id).innerHTML = msg;
}

function uploadJar() {
    var jar = document.getElementById('jar');
    var fileName = jar.value;
    var i = fileName.lastIndexOf("fakepath");
    fileName = fileName.substring(i + 9);
    var inputName = document.getElementById('jarName').value;

    if (inputName == "..." || inputName != fileName) {
        setInnerHtmlValue("jarUploadInfo", "请选择文件！")
        return;
    }

    var formData = new FormData();
    formData.append('jar', $('#jar')[0].files[0]);

    $.ajax({
        url: '/file/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function (res) {

        if (res.statusCode == 200) {
            setInnerHtmlValue("jarUploadInfo", "Jar包上传成功！")
        } else if (res.statusCode == 701) {
            setInnerHtmlValue("jarUploadInfo", "Jar包已存在！")
        } else if (res.statusCode == 500) {
            setInnerHtmlValue("jarUploadInfo", "服务器内部错误！")
        }
    }).fail(function (res) {
        console.log(res);
    });
}