function toggleFiltersDisplay() {
  jQuery("#filters").toggle(500, function() {
    jQuery("#filters-icon").toggleClass("icon-angle-down");
    jQuery("#filters-icon").toggleClass("icon-angle-up");
  });
}

jQuery(function() {
  toggleFiltersDisplay();
  jQuery("#patient-search-clear-button").hide();
  jQuery("#patient-search-results").hide();
  jQuery("#patient-search").change(function() {
    if (jQuery("#patient-search").attr("selected_uuid")) {
      jQuery("#patient-display").text(jQuery("#patient-search").attr("selected_name"));
      jQuery("#patient-id").val(jQuery("#patient-search").attr("selected_uuid"));
      jQuery("#patient-search-results").hide();
    }
  });
  jQuery("#patient-search").keyup(function() {
    jQuery("#patient-search-results").show();
  });
});

window.onload = function() {
  jQuery("#patient-search-results-table_wrapper").removeClass("dataTables_wrapper");
}