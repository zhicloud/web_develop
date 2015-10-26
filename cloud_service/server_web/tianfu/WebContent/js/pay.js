 


var MERCHANTID ='105290045112144';              //�Ϻ�֧�� 
var POSID='100000037';                 //���д���
var BRANCHID='310000000'; 

var INTER_FLAG='0';						//�ӿ����ͣ���Ϊ'1'���ʾ�½ӿڣ��½ӿڱ������̻���Կ��ǰ30λPUB32
var PUB32='9325a01ffe84834f2713709b020111';

 
           
var ORDERID=Math.round(Math.random()*100000)+1;//'001';

var PAYMENT='0.01';
var CURCODE='01';
var TXCODE='520100';
var OPERID='140828008-999';
var AUTHID='P0123456';
var PASSWORD='111111';
var REQUESTSN='sss';
var REMARK1='';
var REMARK2='';


var bankURL='http://128.128.96.175:8001/app/ccbMain'; //sit
 


 
var strMD5; 
/**
 * 设置默认的初始值
 */
function setValue()
{
	
	var objMERCHANTID=document.getElementById("MERCHANTID");
	objMERCHANTID.value=MERCHANTID;
	
	
	var objPOSID=document.getElementById("POSID");
	objPOSID.value=POSID;	
	
	
	var objBRANCHID=document.getElementById("BRANCHID");
	objBRANCHID.value=BRANCHID;
	
	
	var objORDERID=document.getElementById("ORDERID");
	objORDERID.value=Math.round(Math.random()*100000)+1;

	
	
	
	var objPAYMENT=document.getElementById("PAYMENT");
	objPAYMENT.value=PAYMENT;
	
	
	
	var objCURCODE=document.getElementById("CURCODE");
	objCURCODE.value=CURCODE;
	
	
	var objTXCODE=document.getElementById("TXCODE");
	objTXCODE.value=TXCODE;
	
	
	var objREMARK1=document.getElementById("REMARK1");
	objREMARK1.value=REMARK1;
	
	
	var objREMARK2=document.getElementById("REMARK2");
	objREMARK2.value=REMARK2;
	
	
	var objbankURL=document.getElementById("bankURL");
	objbankURL.value=bankURL;

	
	document.getElementById("PUB32").value=PUB32;;
	document.getElementById("INTER_FLAG").value=INTER_FLAG;; 

}

/**
 * 根据信息获取MAC（MD5算法）
 */
function makeMD5()
{
	
	var tmp;
	var tmp0;
	var tmp1;
    var temp_New1;
  MERCHANTID=document.getElementById("MERCHANTID").value;
  
	
	POSID=document.getElementById("POSID").value;
	
	
	BRANCHID=document.getElementById("BRANCHID").value;
	
	
	ORDERID=document.getElementById("ORDERID").value;
	
	
	
	PAYMENT=document.getElementById("PAYMENT").value;
	

	CURCODE=document.getElementById("CURCODE").value;
	
	
	TXCODE=document.getElementById("TXCODE").value;
	

	REMARK1=document.getElementById("REMARK1").value;
	
	
	REMARK2=document.getElementById("REMARK2").value;
	
	

	

    
	tmp ='MERCHANTID='+MERCHANTID+'&POSID='+POSID+'&BRANCHID='+BRANCHID+'&ORDERID='+ORDERID+'&PAYMENT='+PAYMENT+'&CURCODE='+CURCODE+'&TXCODE='+TXCODE+'&REMARK1='+REMARK1+'&REMARK2='+REMARK2;
	tmp0='MERCHANTID='+MERCHANTID+'&POSID='+POSID+'&BRANCHID='+BRANCHID+'&ORDERID='+ORDERID+'&PAYMENT='+PAYMENT+'&CURCODE='+CURCODE+'&TXCODE=520100'+'&REMARK1='+REMARK1+'&REMARK2='+REMARK2;
	
	index=document.getElementById("INTER_FLAG").value;
	temp_New=tmp; 
	 

	if(index=="1"){
		temp_New=tmp+'&PUB32='+document.getElementById("PUB32").value;
	}
	if(index=="3"){
		
		temp_New=tmp+'&TYPE=1&PUB='+document.getElementById("PUB32TR2").value+'&GATEWAY='+document.getElementById("GATEWAY").value+'&CLIENTIP='+document.getElementById("CLIENTIP").value+'&REGINFO='+document.getElementById("REGINFO").value+'&PROINFO='+document.getElementById("PROINFO").value+'&REFERER='+document.getElementById("MER_REFERER").value;
		temp_New1=tmp+'&TYPE=1&GATEWAY='+document.getElementById("GATEWAY").value+'&CLIENTIP='+document.getElementById("CLIENTIP").value+'&REGINFO='+document.getElementById("REGINFO").value+'&PROINFO='+document.getElementById("PROINFO").value+'&REFERER='+document.getElementById("MER_REFERER").value;
	}

	if(index=='2'){
		tmp ='MERCHANTID='+MERCHANTID+'&POSID='+POSID+'&BRANCHID='+BRANCHID+'&ORDERID='+ORDERID+'&AUTHID='+AUTHID+'&USERID='+OPERID+'&PASSWORD='+PASSWORD+'&PAYMENT='+PAYMENT+'&REQUESTSN='+REQUESTSN+'&CURCODE='+CURCODE+'&TXCODE='+TXCODE+'&REM1='+REMARK1+'&REM2='+REMARK2;
		temp_New=tmp+'&PUB32='+document.getElementById("PUB32").value;
		document.getElementById('REM1').value=document.getElementById('REMARK1').value;
		document.getElementById('REM2').value=document.getElementById('REMARK2').value;
	}
	strMD5=hex_md5(temp_New); 


	
	document.getElementById("MAC").value=strMD5; 
}

/**
 * 提交表单信息
 */
function go()
{
    // 提交请求前，MD5
	makeMD5();
	var objMD5form=document.getElementById("MD5form"); 

    
	objMD5form.submit();

}


function DOTYPE_onclick()
{
var index=window.MD5form.INTER_FLAG.selectedIndex;
var value=window.MD5form.INTER_FLAG.options[index].value;
if (value==0)
{

document.getElementById("PUB32TR1").style.display='none';
document.getElementById("GATEWAYTR").style.display='none';
document.getElementById("CLIENTIPTR").style.display='none';
document.getElementById("REGINFOTR").style.display='none';
document.getElementById("PROINFOTR").style.display='none';
document.getElementById("MER_REFERERTR").style.display='none';


}else if(value==1){

document.getElementById("PUB32TR").style.display='';
document.getElementById("PUB32TR1").style.display='none';
document.getElementById("GATEWAYTR").style.display='none';
document.getElementById("CLIENTIPTR").style.display='none';
document.getElementById("REGINFOTR").style.display='none';
document.getElementById("PROINFOTR").style.display='none';
document.getElementById("MER_REFERERTR").style.display='none';



}
else if(value==3){

document.getElementById("PUB32TR").style.display='none';
document.getElementById("PUB32TR1").style.display='';
document.getElementById("GATEWAYTR").style.display='';
document.getElementById("CLIENTIPTR").style.display='';
document.getElementById("REGINFOTR").style.display='';
document.getElementById("PROINFOTR").style.display='';
document.getElementById("MER_REFERERTR").style.display='';

}

}

