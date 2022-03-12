app.controller('csController', function ($scope, $controller, $http) {

    $controller('baseController', {$scope: $scope});

    $scope.searchCsInfoNameLike = "";

    // page：第几页；row：每页几行
    $scope.getCsInfos = function (page, row) {
        $http({
            method: "POST",
            url: "/cs/getCsInfos?nameLike=" + $scope.searchCsInfoNameLike + "&page=" + page + "&row=" + row,
        }).then(function successCallback(response) {
            console.log(response);
            $scope.csInfosPagination.totalItems = response.data.total;  // 更新总记录数
            $scope.skeletonInfos = response.data.rows;
        }, function errorCallback(response) {

        });
    };

    $scope.searchSkeletons = function () {
        var searchSkeletonsNameLike = document.getElementById("searchSkeletonsNameLike");
        var searchSkeletonsNameLikeValue = searchSkeletonsNameLike.value;
        $scope.searchCsInfoNameLike = searchSkeletonsNameLikeValue;
        $scope.getCsInfos($scope.csInfosPagination.currentPage, $scope.csInfosPagination.itemsPerPage);
    };

    $scope.csInfosPagination = {
        currentPage: 1,
        totalItems: 8000,
        itemsPerPage: 8,
        pagesLength: 15,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.getCsInfos($scope.csInfosPagination.currentPage, $scope.csInfosPagination.itemsPerPage);
        }
    };

    $scope.cloneSkeletonSpsid = "";
    $scope.cloneSkeletonServiceName = "";
    $scope.cloneSkeletonVersion = "";

    $scope.cloneSkeleton = function (spsid, serviceName, version) {
        $scope.cloneSkeletonSpsid = spsid;
        $scope.cloneSkeletonServiceName = serviceName;
        $scope.cloneSkeletonVersion = version;
    };

    $scope.removeSkeleton = function (scsid, spsid, serviceName, version) {
        $http({
            method: "POST",
            url: "/api/remove-skeleton/" + scsid + "/" + spsid + "/" + serviceName + "/" + version,
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

    $scope.refreshCurrent = function () {
        $scope.getCsInfos($scope.csInfosPagination.currentPage, $scope.csInfosPagination.itemsPerPage);
    };

    $scope.clearCloneInfo = function() {
        setInnerHtmlValue("cloneSkeletonInfo", "");
    };

    $scope.realCloneSkeleton = function () {
        var scsid = document.getElementById("cloneSkeletonScsid").value;
        $http({
            method: "POST",
            url: "/api/build-skeleton/" + scsid + "/" + $scope.cloneSkeletonSpsid + "/" + $scope.cloneSkeletonServiceName + "/" + $scope.cloneSkeletonVersion,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                setInnerHtmlValue("cloneSkeletonInfo", "克隆成功！");
                return;
            }
            setInnerHtmlValue("cloneSkeletonInfo", "克隆失败！");
        }, function errorCallback(response) {

        });
    };

    $scope.getAllScsid = function (spsid, serviceName, version) {
        $http({
            method: "POST",
            url: "/ps/getAllScsid?spsid=" + spsid + "&serviceName=" + serviceName + "&version=" + version,
        }).then(function successCallback(response) {
            $scope.scsids = response.data.rows;

            console.log($scope.scsids);

            $("#cloneSkeletonScsid").selectpicker('refresh');

        }, function errorCallback(response) {

        });
    };

});