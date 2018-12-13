/**
 * Gets posible pages using urlString, resultsPerPage and totalItemsNumber
 */
function getPossiblePages(urlString, resultsPerPage, totalItemsNumber) {
    var pages = [];
    var paginationAttempts = Math.ceil(totalItemsNumber / resultsPerPage);
    // i is the pagecount to display
    var from = 1;
    for (i = 1; i <= paginationAttempts; i++) {
        var to;
        if (i == 1) {
            to = resultsPerPage;
        } else {
            if (from >= totalItemsNumber) {
                to = totalItemsNumber;
            } else {
                to = (from - 1) + resultsPerPage;
            }
        }
        urlString = replacePaginationInURL(urlString, from, to);
        pages[i] = getPageObject(i, urlString);
        from = to + 1;
    }
    // return compact pages
    return pages.filter(function() {
        return true;
    });
}

/**
 * Constructs a page object with page number and url
 */
function getPageObject(page, pageUrl) {
    // page is non 0 index whereas $scope.pages is
    return {
        "page": page,
        "url": pageUrl
    };
}

/**
 * Removes pagination parameters from a urlString
 */
function removePaginationFromURL(urlString) {
    if (urlString.indexOf("fromItemNumber=") > 0) {
        urlString = urlString.substring(0, urlString.indexOf("fromItemNumber="));
    }
    return urlString;
}

/**
 * Sets angular's scope next  page and previous page
 */
function setNextAndPreviousPages($scope, currentPage) {
    $scope.nextPage = $scope.pages[currentPage.page];
    $scope.previousPage = $scope.pages[currentPage.page - 2];
}

/**
 * Initialises angular pages to display on a page
 */
function renderPagination($scope, pagination, initialisePages) {
    if (isEmpty(pagination.toItemNumber) || pagination.totalItemsNumber <= pagination.toItemNumber) {
        pagination.toItemNumber = pagination.totalItemsNumber;
    }
    if (pagination.totalItemsNumber == 0) {
        pagination.fromItemNumber = 0;
    }
    if (initialisePages) {
        // render pagination on first page load or resultsPerPage change
        $scope.pages = [];
        // one page
        if (pagination.toItemNumber == pagination.totalItemsNumber) {
            if (pagination.totalItemsNumber > 0) {
                $scope.pages[1] = getPageObject(1, url);
                $scope.nextPage = undefined;
                $scope.previousPage = undefined;
            }
        } else { // more than one pages
            $scope.pages = getPossiblePages(url, parseInteger($scope.resultsPerPage), pagination.totalItemsNumber);
            setNextAndPreviousPages($scope, $scope.pages[0]);
        }
        $scope.currentPage = 1;
    }
}

/**
 * Replace pagination params from in if they exist
 */
function replacePaginationInURL(urlString, from, to) {
    urlString = removePaginationFromURL(urlString);
    if(urlString.indexOf("?") == -1) {
    	urlString += "?fromItemNumber=" + from;
    } else {
    	urlString += "&fromItemNumber=" + from;
    }
    return (urlString + "&toItemNumber=" + to).replace("?&", "?");
}

/**
 * Replaces pagination in a global url variable
 */
function replacePaginationInURLToRetrieveAll($scope) {
    $scope.resultsPerPage = "all";
    url = replacePaginationInURL(url, "1", $scope.resultsPerPage);
}

/**
 * Navigates to a given page selected or next/previous
 */
function paginate($scope, page, callback) {
    if (page && $scope.currentPage != page.page) {
        url = page.url;
        $scope.currentPage = page.page;
        callback();
        setNextAndPreviousPages($scope, page);
    }
}

/**
 * Updates the number of results to show per page
 */
function pagination($scope, callback) {
    // remove and re-add pagination to url
    url = replacePaginationInURL(url, "1", $scope.resultsPerPage);
    // retrive new results by new url
    callback();
}