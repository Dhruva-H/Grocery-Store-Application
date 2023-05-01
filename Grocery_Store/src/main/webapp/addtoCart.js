	function addtoCart(itemID, quantity){
		const xhttp = new XMLHttpRequest;
		xhttp.onload = function(){
			const yhttp = new XMLHttpRequest();
			yhttp.onload = function(){
				document.getElementById("itemsList").innerHTML = this.responseText;
			}
			yhttp.open("POST", "displayItems.jsp");
			yhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			yhttp.send("category=Food&pageNo=0");
		}
		xhttp.open("POST", "cartHandler");
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhttp.setRequestHeader("itemID", itemID);
		xhttp.setRequestHeader("quantity", quantity);
		xhttp.send("method=add");
	}
	