<%
    def options = ableToWrite
    options = options.collect {
        def selected = (it == config.initialValue);
        [ label: it.label, value: it.value, selected: selected ]
    }
    options = options.sort { a, b -> a.label <=> b.label }
%>
${ ui.includeFragment("uicommons", "field/dropDown", [ options: options, initialValue: ui.escapeAttribute(uiUtils.getAttribute(patient, ableToWriteUuid)) ] << config) }