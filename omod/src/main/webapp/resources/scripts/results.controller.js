var app = angular.module("resultsApp", []);
var url;
var patientId;
var category;
var initialPageLoad = false;
var retrieveResults;

app.controller("ResultsController", ResultsController);
ResultsController.$inject = ['$scope', '$sce'];

function ResultsController($scope, $sce) {
    $scope.resultsPerPage = "25";
    this.retrieveResults = this.retrieveResults || function(initialisePages, callback) {
        // TODO probably wrap in some loading...
        if (isEmpty(url)) {
            var urlParams = new URLSearchParams(window.location.search);
            patientId = urlParams.get('patientId');
            category = urlParams.get('category');
            url = '/' + OPENMRS_CONTEXT_PATH +
                '/ws/rest/v1/msfcore/resultData?patientId=' + patientId +
                "&category=" + category;
            initialPageLoad = true;
        } else {
            initialPageLoad = false;
        }
        jQuery.get(url, function(data) {
            // initialise results list
            var results = data.results[0];
            if (initialPageLoad) {
                logViewResultsEvent(results);
            }
            if (callback) {
                if (callback.name.indexOf("filterBy") >= 0) {
                    callback(results, $scope);
                }
            } else {
                clearFilterFields($scope, results);
            }
            renderPagination($scope, results.pagination, initialisePages);

            // display results
            $scope.results = results;
            $scope.$apply();
        });
    }
    // caching retrieveResults to make it accessible outside ResultsController
    retrieveResults = this.retrieveResults;

    this.retrieveResultsInitialisePages = this.retrieveResultsInitialisePages || function() {
        retrieveResults(true);
    }

    this.edit = this.edit || function($event, result) {
        // locate result and replace labels with text fields on editable columns
        if (jQuery($event.currentTarget).hasClass("icon-edit")) {
            triggerEditing($scope, jQuery("input.editable,select.editable"), result, true);
            triggerEditing($scope, jQuery("label.editable"), result);
            toggleEditingIcon($event);
        } else {
            var payload = generateResultRowData($scope);
            if (!isEmpty(payload)) {
                var data = {
                    "payload": payload,
                    "category": category
                };
                jQuery.ajax({
                    url: "/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/msfcore/resultRow",
                    type: "POST",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function(response) {
                        console.log("Updated/Added Result at Obs: " + response);
                        retrieveResults(false);
                    }
                });
            } else {
                // TODO use ${ui.message('')}
                alert("Enter correct details before saving!");
            }
        }
    }

    this.purge = this.purge || function(result) {
        // TODO use ${ui.message('msfcore.resultList.confirmCancel')}
        if (confirm("Are you sure you want to Cancel this Record?")) {
            jQuery.ajax({
                url: '/' + OPENMRS_CONTEXT_PATH + '/ws/rest/v1/order/' + result.uuid.value + '?!purge',
                type: 'DELETE',
                success: function(result) {
                    retrieveResults(false);
                }
            });
        }
    }

    this.renderResultValue = this.renderResultValue || function(result, key) {
        var value;
        var claz = result[key].editable ? "editable" : "";
        var time = "";
        if (resultPendingWhenEditable(result, key, $scope.results.resultCategory)) {
            value = result.status.value;
            claz += " column-status";
        } else {
            value = result[key].value;
            if (result[key].type == "DATE") {
                if (!isEmpty(value)) {
                    time = value;
                    value = convertToDateFormat($scope.results.dateFormatPattern, new Date(value));
                }
            }
        }
        var valueHtml;
        if ($scope.results.resultCategory == "DRUG_LIST" && result[key].type == "STOP") {
            var discontinueable = false;
            if (!value && result.status.value == "ACTIVE") { // confirm it's discontinueable
                discontinueable = true;
            }
            var disabled = !discontinueable ? "disabled" : "";
            var checked = value ? "checked" : "";
            var title = value ? ("title='" + convertToDateFormat($scope.results.dateFormatPattern, new Date(result[key].stopDate)) + "'") : "";
            var discontinueableHtml = discontinueable ? "onchange=\"discontinue(this,'" + result.uuid.value + "')\"" : "";
            valueHtml = "<input type='checkbox' class='actionable' " + checked + " " + disabled + " " + discontinueableHtml + " " + title + "/>"
        } else {
            if (isEmpty(value)) {
                value = "";
            }
            var concept = result.concept ? result.concept.value : "";
            var valueProperties = "id='" + result.uuid.value + "_" + $scope.results.keys.indexOf(key) + "_" + result[key].type + "_" +
                concept + "' class='" + claz + "' time='" + time + "'";
            valueHtml = "<label " + valueProperties + ">" + value + "</label>";
        }
        return $sce.trustAsHtml(valueHtml);
    }

    this.nameFilter = this.nameFilter || function() {
        if (!isEmpty(jQuery("#filter-name").val())) {
            replacePaginationInURLToRetrieveAll($scope);
            retrieveResults(true, filterByName);
        }
    }

    this.statusFilter = this.statusFilter || function() {
        replacePaginationInURLToRetrieveAll($scope);
        retrieveResults(true, filterByStatus);
    }

    this.datesFilter = this.datesFilter || function() {
        if (isValidDate(jQuery("#filter-start-date").val()) && isValidDate(jQuery("#filter-end-date").val())) {
            replacePaginationInURLToRetrieveAll($scope);
            retrieveResults(true, filterByDates);
        }
    }
    
    this.providerFilter = this.providerFilter || function() {
        replacePaginationInURLToRetrieveAll($scope);
        retrieveResults(true, filterByProvider);
    }

    $scope.retrieveResults = retrieveResults;
    $scope.retrieveResultsInitialisePages = this.retrieveResultsInitialisePages;
    $scope.edit = this.edit;
    $scope.purge = this.purge;
    $scope.paginate = paginate;
    $scope.pagination = pagination;
    $scope.renderResultValue = this.renderResultValue;
    $scope.nameFilter = this.nameFilter;
    $scope.statusFilter = this.statusFilter;
    $scope.datesFilter = this.datesFilter;
    $scope.providerFilter = this.providerFilter;
}

function replaceElementWithTextInput($scope, result, element) {
    element.replaceWith(function() {
        var id = jQuery(this).attr('id');
        var time = jQuery(this).attr('time');
        var type = id.split("_")[2];
        var key = $scope.results.keys[id.split("_")[1]];
        var originalValue = jQuery(this).text();
        var originalClass = jQuery(this).attr('class');
        var value = originalValue.replace("PENDING", "").replace("CANCELLED", "");
        if (type == "DATE" && !isEmpty(time)) {
            value = convertToDatePickerDateFormat(new Date(parseInteger(time)));
        }
        var sharedAttributes = "class='editable' original_class='" + originalClass + "' original_value='" + originalValue + "' id='" + id + "' value='" + value + "' time='" + time + "'";
        var inputElement = "<input " + sharedAttributes + " type='" + type + "' />";
        if (type == "BOOLEAN") {
            var selectedTrue = value == "true" ? " selected" : "";
            var selectedFalse = value == "false" ? " selected" : "";
            inputElement = "<select " + sharedAttributes + ">" +
                "<option value='true'" + selectedTrue + ">True</option>" +
                "<option value='false'" + selectedFalse + ">False</option>" +
                "</select>";
        } else if (type == "CODED") {
            inputElement = "<select " + sharedAttributes + "><option/>";
            var codedOptions = result[key].codedOptions;
            for (i in codedOptions) {
                var selected = value == codedOptions[i].name ? " selected" : "";
                inputElement += "<option value='" + codedOptions[i].uuid + "' " + selected + ">" + codedOptions[i].name + "</option>";
            }
            inputElement += "</select>";
        }
        return jQuery(inputElement);
    });
}

function replaceElementWithLabel(element) {
    element.replaceWith(function() {
        var id = jQuery(this).attr('id');
        var time = jQuery(this).attr('time');
        var value = jQuery(this).attr('original_value');
        var claz = jQuery(this).attr('original_class');
        return jQuery("<label class='" + claz + "' id='" + id + "' time='" + time + "'>" + value + "<label/>");
    });
}

function triggerEditing($scope, element, result, edit) {
    element.each(function(key, value) {
        var elementId = value.getAttribute('id');
        if (edit) {
            // re-render all edits back to labels
            replaceElementWithLabel(jQuery("#" + elementId));
            jQuery(".icon-ok").addClass("icon-edit").removeClass("icon-ok");
        } else {
            if (elementId.startsWith(result.uuid.value)) {
                replaceElementWithTextInput($scope, result, jQuery("#" + elementId));
            }
        }
    });
}

function toggleEditingIcon($event) {
    jQuery($event.currentTarget).toggleClass("icon-edit");
    jQuery($event.currentTarget).toggleClass("icon-ok");
}

function generateResultRowData($scope) {
    var data = [];
    jQuery("input.editable,select.editable").each(function(key, value) {
        var id = value.getAttribute('id');
        var value = jQuery("#" + id).val();
        var type = id.split("_")[2];
        if (type == "DATE") {
            if (isValidDate(value)) {
                value = convertToDateFormat($scope.results.dateFormatPattern, new Date(value));
            } else {
                value = "";
            }
        }
        // TODO support validation and on fail return empty array
        if (!isEmpty(value)) {
            data.push({
                "uuid_key_type_concept": id,
                "value": value
            });
        }
    });
    return data;
}

function applyFilterChanges(results) {
    // remove any pending empty results because of excluding some
    results.results = results.results.filter(function() {
        return true;
    });
    // update totalNumber of results
    results.pagination.totalItemsNumber = results.results.length;
}

function filterByName(results, $scope) {
    const name = jQuery("#filter-name").val();
    jQuery.each(results.results, function(i, resultRow) {
        if (resultRow[results.filters.name].value.toLowerCase().indexOf(name.toLowerCase()) == -1) {
            results.results = removeItemAtIndex(results.results, i);
        }
    });
    applyFilterChanges(results);
    clearFilterFields($scope, results);
    jQuery("#filter-name").val(name);
}

function filterByStatus(results, $scope) {
    const status = jQuery("#filter-status").val();
    if (status != "all") {
        jQuery.each(results.results, function(i, resultRow) {
            if (resultRow.status.value != status) {
                results.results = removeItemAtIndex(results.results, i);
            }
        });
    }
    applyFilterChanges(results);
    clearFilterFields($scope, results);
    jQuery("#filter-status").val(status);
}

function removeItemAtIndex(items, index) {
    delete items[index];
    return items;
}

function clearFilterFields($scope, results) {
    jQuery("#filter-name").val("");
    jQuery("#filter-status").val("all");
    jQuery("#filter-provider").val("all");
    jQuery("#filter-start-date").val("");
    jQuery("#filter-end-date").val("");
    if (results.filters.dates && results.filters.dates.length > 0) {
    	jQuery("#filter-dates").val(results.filters.dates[0]);
        $scope.filterDateValue = results.filters.dates[0];
    }
    $scope.filterStartDate = "";
    $scope.filterEndDate = "";
    $scope.filterStatusValue = "all";
    $scope.filterProviderValue = "all";
}

function filterByDates(results, $scope) {
    const startDate = jQuery("#filter-start-date").val();
    const endDate = jQuery("#filter-end-date").val();
    const dateField = jQuery("#filter-dates").val();
    jQuery.each(results.results, function(i, resultRow) {
        // matching against all dates options
        if (dateField.toLowerCase() == "all") {
            for (d in results.filters.dates) {
                removeResultsNonMatchedByDates(results.filters.dates[d], i, resultRow, results, startDate, endDate);
            }
        } else {
            // the conversion removes time
            removeResultsNonMatchedByDates(dateField, i, resultRow, results, startDate, endDate);
        }
    });
    applyFilterChanges(results);
    clearFilterFields($scope, results);
    jQuery("#filter-dates").val(dateField);
    jQuery("#filter-start-date").val(startDate)
    jQuery("#filter-end-date").val(endDate)
}

function filterByProvider(results, $scope) {
	const provider = jQuery("#filter-provider").val();
    if (provider != "all") {
        jQuery.each(results.results, function(i, resultRow) {
        	// uses msfcore.provider message index, update when keys arrangment changes
            if (results.resultCategory == 'REFERRAL_LIST' && resultRow[results.keys[3]].value != provider) {
                results.results = removeItemAtIndex(results.results, i);
            }
        });
    }
    applyFilterChanges(results);
    clearFilterFields($scope, results);
    jQuery("#filter-provider").val(status);
}

function removeResultsNonMatchedByDates(dateField, i, resultRow, results, startDate, endDate) {
    var dateString = convertToDatePickerDateFormat(new Date(parseInteger(resultRow[dateField].value)));
    var date = new Date(dateString);
    if (!isValidDate(dateString) || date.getTime() < new Date(startDate).getTime() || date.getTime() > new Date(endDate).getTime()) {
        results.results = removeItemAtIndex(results.results, i);
    }
}

function logViewResultsEvent(results) {
    var event;
    if (results.resultCategory == "LAB_RESULTS") {
        event = "VIEW_LAB_RESULTS";
    } else if (results.resultCategory == "DRUG_LIST") {
        event = "VIEW_DRUG_DISPENSING";
    } else if (results.resultCategory == "REFERRAL_LIST") {
        event = "VIEW_PATIENT_REFERRALS";
    }
    if (event) {
        var data = {
            "event": event,
            "patient": results.patient
        };
        jQuery.ajax({
            url: "/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/msfcore/auditlog",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8"
        });
    }
}

function resultPendingWhenEditable(result, key, category) {
    var pendingWhenEditable = result[key].editable;
    if (category == "LAB_RESULTS") {
        pendingWhenEditable = pendingWhenEditable && result.status.value != 'COMPLETED';
    } else if (category == "DRUG_LIST") {
        pendingWhenEditable = pendingWhenEditable && (result.status.value == 'PENDING' || result.status.value == 'CANCELLED');
    } else if (category == "REFERRAL_LIST") {
        pendingWhenEditable = pendingWhenEditable && (result.status.value == 'PENDING' || result.status.value == 'CANCELLED');
    }
    return pendingWhenEditable;
}

function discontinue(element, resultUuid) {
    if (!isEmpty(resultUuid)) {
        // TODO use ${ui.message('msfcore.resultList.discontinueOrder')}
        if (confirm("Are you sure you want to discontinue this medication?")) {
            jQuery.get("/" + OPENMRS_CONTEXT_PATH + "/ws/rest/v1/msfcore/discontinueorder?category=" + category + "&uuid=" + resultUuid, function(data) {
                if (data) {
                    console.log(resultUuid + " order has been discontinued by: " + data.results[0].uuid);
                    retrieveResults(false);
                }
            });
        } else {
            jQuery(element).prop("checked", !jQuery(element).prop("checked"));
        }
    }
}