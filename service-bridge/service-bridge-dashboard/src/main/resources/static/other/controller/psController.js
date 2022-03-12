app.controller('psController', function ($scope, $controller, $http) {

    $controller('baseController', {$scope: $scope});

    $scope.searchPsInfoNameLike = "";

    // page：第几页；row：每页几行
    $scope.getPsInfos = function (page, row) {
        $http({
            method: "POST",
            url: "/ps/getPsInfos?nameLike=" + $scope.searchPsInfoNameLike + "&page=" + page + "&row=" + row,
        }).then(function successCallback(response) {
            console.log(response);
            $scope.psInfosPagination.totalItems = response.data.total;  // 更新总记录数
            $scope.stubInfos = response.data.rows;
        }, function errorCallback(response) {

        });
    };

    $scope.clearBuildSkeletonInfo = function () {
        setInnerHtmlValue("buildSkeletonInfo", "");
    };

    $scope.clearBuildStubInfo = function () {

        setInnerHtmlValue("buildStubInfo", "");

    };

    $scope.psInfosPagination = {
        currentPage: 1,
        totalItems: 8000,
        itemsPerPage: 8,
        pagesLength: 15,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.getPsInfos($scope.psInfosPagination.currentPage, $scope.psInfosPagination.itemsPerPage);
        }
    };

    $scope.getPsJarName = function (spsid) {
        $http({
            method: "POST",
            url: "/ps/getPsJarName?spsid=" + spsid,
        }).then(function successCallback(response) {
            $scope.jarNames = response.data.rows;

            console.log($scope.jarNames);

            $("#buildStubJarName").selectpicker('refresh');

        }, function errorCallback(response) {

        });
    };

    $scope.removeStub = function (spsid, jarName, serviceName, version) {
        $http({
            method: "POST",
            url: "/api/remove-stub/" + spsid + "/" + jarName + "/" + serviceName + "/" + version,
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

    $scope.searchStubs = function () {
        var searchStubsNameLike = document.getElementById("searchStubsNameLike");
        var searchStubsNameLikeValue = searchStubsNameLike.value;
        $scope.searchPsInfoNameLike = searchStubsNameLikeValue;
        $scope.getPsInfos($scope.psInfosPagination.currentPage, $scope.psInfosPagination.itemsPerPage);
    };

    $scope.refreshCurrent = function () {
        $scope.getPsInfos($scope.psInfosPagination.currentPage, $scope.psInfosPagination.itemsPerPage);
    };

    $scope.buildStubSpsid = "";
    $scope.buildStubServiceName = "";
    $scope.buildStubVersion = "";

    $scope.buildStub = function (spsid, serviceName, version) {
        $scope.buildStubSpsid = spsid;
        $scope.buildStubServiceName = serviceName;
        $scope.buildStubVersion = version;
    };

    $scope.buildSkeletonSpsid = "";
    $scope.buildSkeletonServiceName = "";
    $scope.buildSkeletonVersion = "";

    $scope.buildSkeleton = function (spsid, serviceName, version) {
        setInnerHtmlValue("buildStubInfo", "");
        $scope.buildSkeletonSpsid = spsid;
        $scope.buildSkeletonServiceName = serviceName;
        $scope.buildSkeletonVersion = version;

    };

    $scope.realBuildStub = function () {

        setInnerHtmlValue("buildStubInfo", "正在构建，请等待！");
        var jarName = document.getElementById("buildStubJarName").value;
        $http({
            method: "POST",
            url: "/api/build-stub/" + $scope.buildStubSpsid + "/" + jarName + "/" + $scope.buildStubServiceName + "/" + $scope.buildStubVersion,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                setInnerHtmlValue("buildStubInfo", "构建成功！");
                return;
            }
            setInnerHtmlValue("buildStubInfo", "构建失败！");
        }, function errorCallback(response) {

        });
    };

    $scope.realBuildSkeleton = function () {
        var scsid = document.getElementById("buildSkeletonScsid").value;

        $http({
            method: "POST",
            url: "/api/build-skeleton/" + scsid + "/" + $scope.buildSkeletonSpsid + "/" + $scope.buildSkeletonServiceName + "/" + $scope.buildSkeletonVersion,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                setInnerHtmlValue("buildSkeletonInfo", "构建成功！");
                return;
            }
            setInnerHtmlValue("buildSkeletonInfo", "构建失败！");
        }, function errorCallback(response) {

        });
    };

    $scope.clearBuildSkeletonManualInfo = function (){
        setInnerHtmlValue("buildSkeletonManualInfo", "");
    };
    $scope.buildSkeletonManualSpsid = "";
    $scope.buildSkeletonManualJarName = "";
    $scope.buildSkeletonManualServiceName = "";
    $scope.buildSkeletonManualVersion = "";

    $scope.realBuildSkeletonManual = function() {

        if($scope.buildSkeletonManualSpsid == "" || $scope.buildSkeletonManualJarName == "" || $scope.buildSkeletonManualServiceName == "" || $scope.buildSkeletonManualVersion == ""){
            setInnerHtmlValue("buildSkeletonManualInfo", "请输入数据！");
            return;
        }

        $http({
            method: "POST",
            url: "/api/build-stub/" + $scope.buildSkeletonManualSpsid + "/" + $scope.buildSkeletonManualJarName + "/" + $scope.buildSkeletonManualServiceName + "/" + $scope.buildSkeletonManualVersion,
        }).then(function successCallback(response) {
            console.log(response);
            if (response.data.statusCode == 200) {
                setInnerHtmlValue("buildSkeletonManualInfo", "构建成功！");
                return;
            }
            setInnerHtmlValue("buildSkeletonManualInfo", "构建失败！");
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

            $("#buildSkeletonScsid").selectpicker('refresh');

        }, function errorCallback(response) {

        });
    };


});


function setInnerHtmlValue(id, msg) {
    document.getElementById(id).innerHTML = msg;
}