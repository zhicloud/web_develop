
$(function(){
	$.jgrid.defaults.width = '100%';
	$.jgrid.defaults.responsive = true;
	$.jgrid.defaults.styleUI = 'Bootstrap';
	var jqGridHeight = $('body').height()-303;
	$("#jqGrid").jqGrid({
        url: 'data.json',
        datatype: 'json',
        colModel: [			
			{ label: 'ID', name: 'ProductID', width: 45, key: true },
			{ label: 'Category Name', name: 'CategoryName', width: 75 },
			{ label: 'Product Name', name: 'ProductName', width: 90 },
			{ label: 'Country', name: 'Country', width: 100 },
			{ label: 'Price', name: 'Price', width: 80, sorttype: 'integer' },
			{ label: 'Quantity', name: 'Quantity', width: 80, sorttype: 'number' }                   
        ],
		loadonce: true,
		viewrecords: true,
		autowidth:true,
		width: '100%',
		height: jqGridHeight,
        rowNum: '20',
        rownumbers: false, 
        rownumWidth: '25',
        multiselect: true,
        pager: "#jqGridPager"
    });
})