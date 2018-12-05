/**
 * Print html from entire page hiding and/or showing some elements
 */
function printPageWithIgnoreInclude(elementsToIgnore, elementsToInclude) {
	hideElements(elementsToIgnore);
	showElements(elementsToInclude);
    window.print();
    showElements(elementsToIgnore);
    hideElements(elementsToInclude);
}

function hideElements(elements) {
	if(elements) {
		jQuery(elements).hide();
	}
}

function showElements(elements) {
	if(elements) {
		jQuery(elements).show();
	}
}

/**
 * element, html element to write table to
 * items, items array
 * breakPoint, number to break table at horinzotally
 */
function tabulateCleanedItemsIntoAnElement(element, items, breakPoint) {
    if (element && items) {
        var tableContent = "<table>";
        var lastNonZeroBreakIndex;
        // remove empty items if any
        items = items.filter(function(el) {
            return el != null;
        });
        for (i = 0; i < items.length; i++) {
            // build horizontally at first item or after break point
            if (i == 0 || tableContent.endsWith("</tr>")) {
                tableContent += "<tr>";
            }
            var item = items[i];
            // clean up item
            if (item.endsWith(", ")) {
                item = item.substring(0, item.length - 2);
            }
            // build table data/records
            tableContent += "<td>" + item + "</td>";
            // break table vertically at first break point or last item, or next
            // break point
            if (i + 1 == breakPoint || i == items.length - 1 ||
                breakPoint == ((i + 1) - lastNonZeroBreakIndex)) {
                tableContent += "</tr>";
                lastNonZeroBreakIndex = i + 1;
            }
        }
        tableContent += "</table>"
        jQuery(element).html(tableContent);
    }
}

/*
 * Populating Follow-up tables and adding to follow-up div
 */
function tabulateCleanedItemsIntoAnElementFupNote(element, items, newTableIndex) {
	if (element && items) {
		var tableContent = "<table>";
		var div;
		// remove empty items if any
		items = items.filter(function(el) {
			return el != null;
		});
		for (i = 0; i < items.length; i++) {
			if(i % newTableIndex === 0){
				if(tableContent.endsWith("</tr>")){
					tableContent += "</table>"
					jQuery(div).html(tableContent);
					if(items[i] == '0'){
						break;
					}
				}
				jQuery(element).append("<h4>Followup Note - " + items[i] + "</h4>");
				jQuery(element).append("<div id=div-"+i+"></div>");
				div = "#div-"+i;
				tableContent = "<table>";
				continue;
			}
			// build horizontally at first item or after break point
			if (i % newTableIndex === 1 || tableContent.endsWith("</tr>")) {
				tableContent += "<tr>";
			}
			var item = items[i];
			// clean up item
			if (item.endsWith(", ")) {
				item = item.substring(0, item.length - 2);
			}
			// build table data/records
			tableContent += "<td>" + item + "</td>";
			tableContent += "</tr>";
		}
	}
}

//Adding header to table
function tabulateCleanedItemsIntoAnElementWithHeader(tableId, items, breakPoint) {
	if (tableId && items && items.length > 0) {
		var lastNonZeroBreakIndex;
		// remove empty items if any
		items = items.filter(function(el) {
			return el != null;
		});
		for (i = 0; i < items.length; i++) {
			// build horizontally at first item or after break point
			if (i == 0) {
				jQuery(tableId+" tr:last").after("<tr>");
			}
			var item = items[i];
			// clean up item
			if (item.endsWith(", ")) {
				item = item.substring(0, item.length - 2);
			}
			// build table data/records
			jQuery(tableId+" tr:last").append("<td>" + item + "</td>");
			// break table vertically at first break point or last item, or next
			// break point
			if (i + 1 == breakPoint || i == items.length - 1
					|| breakPoint == ((i + 1) - lastNonZeroBreakIndex)) {
				jQuery(tableId+" tr:last").append("</tr>");
				lastNonZeroBreakIndex = i + 1;
				if(i+1 < (items.length) ){
					jQuery(tableId+" tr:last").after("<tr>");
				}
			}
		}
	}
	else{
		jQuery(tableId).hide();
	}
}

/**
 * Check if an object is not existing or empty/blank
 */
function isEmpty(object) {
    if (typeof object == "boolean") {
        return false;
    }
    return !object || object == null || object == undefined || object == "" || object.length == 0;
}

/**
 * Check if a dateString evaluates to a valid date
 */
function isValidDate(dateString) {
    if (!isEmpty(dateString)) {
        return !isNaN(new Date(dateString).getTime());
    }
}

/**
 * Convert string to integer and if it's all return max int
 */
function parseInteger(string) {
    if (string == 'all') {
        return Number.MAX_SAFE_INTEGER;
    } else {
        return parseInt(string);
    }
}

/**
 * Converts a date to a given dateFormat pattern (transforms the pattern if java to js)
 */
function convertToDateFormat(dateFormatPattern, date) {
	if(typeof date == "string" || typeof date == "number") {
		date = new Date(date);
	}
    return jQuery.datepicker.formatDate(dateFormatPattern.toLowerCase().replace("yyyy", "yy"), date);
}

/**
 * Converts a date to the default date picker format
 */
function convertToDatePickerDateFormat(date) {
    return jQuery.datepicker.formatDate("yy-mm-dd", date);
}