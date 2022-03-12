app.controller('fileController', function ($scope, $controller, $http) {

    $controller('baseController', {$scope: $scope});

    $scope.searchFileInfoNameLike = "";

    // page：第几页；row：每页几行
    $scope.getFileInfos = function (page, row) {
        $http({
            method: "POST",
            url: "/file/getAllFileInfos?nameLike=" + $scope.searchFileInfoNameLike + "&page=" + page + "&row=" + row,
        }).then(function successCallback(response) {
            console.log(response);
            $scope.fileInfosPagination.totalItems = response.data.total;  // 更新总记录数
            $scope.fileInfos = response.data.rows;
        }, function errorCallback(response) {

        });
    };

    $scope.fileInfosPagination = {
        currentPage: 1,
        totalItems: 8000,
        itemsPerPage: 8,
        pagesLength: 15,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.getFileInfos($scope.fileInfosPagination.currentPage, $scope.fileInfosPagination.itemsPerPage);
        }
    };


    $scope.searchFiles = function () {
        var searchFileInfoNameLike = document.getElementById("searchFileInfoNameLike");
        var searchFileNameLikeValue = searchFileInfoNameLike.value;
        $scope.searchPsInfoNameLike = searchFileNameLikeValue;
        $scope.getPsInfos($scope.psInfosPagination.currentPage, $scope.psInfosPagination.itemsPerPage);
    };

    $scope.downLoadFile = function (fileName) {
        window.open('/file/download?filename=' + fileName);
    };

    $scope.deleteFile = function (fileName) {
        var r = confirm("删除操作不可恢复，请确认是否删除文件：" + fileName);
        if (r == false) {
            return;
        }
        $http({
            method: "POST",
            url: "/file/deleteFile?filename=" + fileName,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                alert("删除文件“" + fileName + "”成功");
                return;
            }
            alert("删除文件“" + fileName + "”失败");
        }, function errorCallback(response) {

        });
    };
});


function setInnerHtmlValue(id, msg) {
    document.getElementById(id).innerHTML = msg;
}