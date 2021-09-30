/* 加载层 */
	var pageii;
	
	function reqLoading(flag){
		if (flag) {
			pageii=$.layer({
			    type: 3,
			   /*  area: ['400px', '530px'], */
			    shade: [0.5, '#fff'],//添加遮罩
			    loading: {
			        type: 1
			    }
			}); 
			
		}else{
			 layer.close(pageii);
		}
		
	}