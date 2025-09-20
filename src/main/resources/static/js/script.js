/**
 * 
 */

const links = document.querySelectorAll('.navbar-nav .nav-link');




    function toggleSidebar() {
      document.getElementById("sidebar").classList.toggle("active");
    }
	
	
	
	//search ber in view contacts
	document.getElementById("viewcontactssearchBox").addEventListener("keyup", function() {
	    let keyword = this.value.trim();
		console.log(keyword);
	    if (keyword.length === 0) {
			document.getElementsByClassName("search-results")[0].style.display = "none";
	        return;
	    }else{
			console.log("......>>>>>");
		document.getElementsByClassName("search-results")[0].style.display = "block";
		}
		
		
		// Safe CSRF fetch
		    let tokenMeta = document.querySelector('meta[name="_csrf"]');
		    let headerMeta = document.querySelector('meta[name="_csrf_header"]');
		    let token = tokenMeta ? tokenMeta.content : "";
		    let header = headerMeta ? headerMeta.content : "X-CSRF-TOKEN";
		fetch("/user/searchContacts", {
		    method: "POST",
		    headers: {
		        "Content-Type": "application/json",
				[header]: token

		    },
		    body: JSON.stringify({ keyword: keyword })
		})
		.then(response => response.json())
		.then(data => {
		    let results = "";
		    if (data.length === 0) {
		        results = "<p>No contacts found</p>";
		    } else {
		        data.forEach(contact => {
		            results += `    
		                    <p><a href="contactDetails/${contact.cid}?page=0">${contact.name}</a></p> 
							
		            `;
		        });
		    }
			console.log(results);
		    document.getElementById("search-results").innerHTML = results;
		})
		.catch(error => console.error("Error fetching contacts:", error));
})


function togglePassword(fieldId) {
    const input = document.getElementById(fieldId);
    const icon = input.nextElementSibling;
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove('bi-eye-fill');
        icon.classList.add('bi-eye-slash-fill');
    } else {
        input.type = "password";
        icon.classList.remove('bi-eye-slash-fill');
        icon.classList.add('bi-eye-fill');
    }
}



function onPay(monthsOfPlan, amount) {
	let token = document.querySelector('meta[name="_csrf"]').content;
	    let header = document.querySelector('meta[name="_csrf_header"]').content;
console.log("onPay")
	fetch("/createOrder", {
	       method: "POST",
	       headers: {
	           "Content-Type": "application/json",
			   [header]: token
	       },
	       body: JSON.stringify({ monthsOfPlan:monthsOfPlan,amount: amount })
	   })
	   .then(res => res.json())
	   .then(order => {
	          console.log("Order created:", order);
			  var options = {
			              key: "rzp_test_RJm6KmTkomgmDR", // <-- replace with your Razorpay Key ID
			              amount: order.amount,
			              currency: order.currency,
			              name: "SmartContacts Premium",
			              description: monthsOfPlan + " months plan",
			              order_id: order.id,
			              handler: function (response) {
			                  // This will be called on successful payment
			                  console.log("Payment success:", response);
			                  // TODO: send response to backend for verification
							  fetch("/verifyPayment", {
							  	       method: "POST",
							  	       headers: {
							  	           "Content-Type": "application/json",
							  			   [header]: token
							  	       },
							  	       body: JSON.stringify({ razorpay_order_id : response.razorpay_order_id,
										razorpay_payment_id : response.razorpay_payment_id,
										razorpay_signature : response.razorpay_signature
									    })
							  	   })
								   .then(res => res.json())
								   .then(res => {
									console.log("payment vefiryed " + res);
									})
								   .catch(err => {
								          console.error("Error verifyPayment:", err);
								      });
			              }, 
						  prefill: {
						                 name: "",
						                 email: "",
						                 contact: ""
						             },
						             theme: {
						                 color: "#3399cc"
						             },
									 // ✅ explicitly allow UPI
									    method: {
									        upi: true,
									        card: true,
									        netbanking: true,
									        wallet: true
									    }
						
			  }
			  var rzp = new Razorpay(options);
			          rzp.open();
})
.catch(err => {
       console.error("Error creating order:", err);
   });

}







