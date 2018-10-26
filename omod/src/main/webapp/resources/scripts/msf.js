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
