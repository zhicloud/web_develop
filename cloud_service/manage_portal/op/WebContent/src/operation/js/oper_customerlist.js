/**
 * customerList JavaScript
 */
$(function(){
	//日历控件
    $('.lhg-calendar').calendar({ 
    		format:'yyyy-MM-dd HH:mm:ss' 
    	});
    //表格
	$.jgrid.defaults.width = '100%';
	$.jgrid.defaults.responsive = true;
	$.jgrid.defaults.styleUI = 'Bootstrap';
	var jqGridHeight = $('body').height()-383;
	$("#jqGrid").jqGrid({
        url: 'data.json',
        datatype: 'json',
        colModel: [			
			{ label: 'ID', name: 'ProductID', width: '45', key: true },
			{ label: 'Category Name', name: 'CategoryName', width: '75' },
			{ label: 'Product Name', name: 'ProductName', width: '90' },
			{ label: 'Country', name: 'Country', width: '100' },
			{ label: 'Price', name: 'Price', width: '80', sorttype: 'integer' },
			{ label: 'Quantity', name: 'Quantity', width: '80', sorttype: 'number' }                   
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

/*$(function(){
	//表格信息
	$('#tabDataGrid').datagrid({
		height:$('body').height()-293,
		fitColumns:true,
		rownumbers:false,
		singleSelect:false,
		pagination:true,
		striped:true,
		fitColumns:true,
		rownumbers:false,
		url:'datagrid_data1.json',
		method:'get',
	    columns:[[
			{field:'ck',checkbox:true},
			{field:'itemid',title:'Name',width:80},
			{field:'productid',title:'Code',width:100},
			{field:'listprice',title:'Name',width:80},
	        {field:'unitcost',title:'Code',width:80},
	        {field:'attr1',title:'Name',width:220},
	        {field:'status',title:'Price',width:60,align:'center'}
	    ]]
	});
	var tabDataGrid = $('#tabDataGrid').datagrid('getPager'); 
    $(tabDataGrid).pagination({ 
        pageSize:10, //每页显示的记录数，默认为10 
        showPageList:false, //定义是否显示可选择的每页记录数
        showRefresh:false, //定义是否显示刷新按钮
        beforePageText:'第&nbsp;', //在输入框之前显示的符号
        afterPageText:'&nbsp;页&nbsp;共&nbsp;{pages}&nbsp;页', //在输入框之后显示的符号
        displayMsg:'当前显示&nbsp;{from}&nbsp;-&nbsp;{to}&nbsp;条记录&nbsp;共&nbsp;{total}&nbsp;条记录'
    });
})*/