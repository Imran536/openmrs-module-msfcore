/*
 * Print html from entire page ignoring some elements
 */
function printPageWithIgnore(elements) {
	jQuery(elements).hide();
	window.print();
	jQuery(elements).show();
}

/*
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
			if (i + 1 == breakPoint || i == items.length - 1
					|| breakPoint == ((i + 1) - lastNonZeroBreakIndex)) {
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
