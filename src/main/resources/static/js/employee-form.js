document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("employeeForm");

    if (!form) {
        return;
    }

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const dob = document.getElementById("dob");
    const department = document.getElementById("department");
    const salary = document.getElementById("salary");
    const manager = document.getElementById("manager");
	const employeeId = document.getElementById("employeeId");
	
	if (employeeId && manager) {

	    const currentEmployeeId =
	        employeeId.value;

	    if (currentEmployeeId) {

	        const currentOption =
	            manager.querySelector(
	                `option[value="${currentEmployeeId}"]`
	            );

	        if (currentOption) {
	            currentOption.disabled = true;
	        }
	    }
	}



    form.addEventListener("submit", function (event) {

        let isValid = true;

        clearClientErrors();

        // First name validation
        if (firstName.value.trim() === "") {
            showError(firstName, "First name is required.");
            isValid = false;
        }

        // Last name validation
        if (lastName.value.trim() === "") {
            showError(lastName, "Last name is required.");
            isValid = false;
        }

        // DOB validation
        if (dob.value === "") {

            showError(dob, "Date of birth is required.");
            isValid = false;

        } else {

            const selectedDate = new Date(dob.value);
            const today = new Date();

            if (selectedDate > today) {

                showError(
                    dob,
                    "Date of birth cannot be in the future."
                );

                isValid = false;
            }
			else { // Calculate the date exactly 18 years ago
			 const minimumDob = new Date( today.getFullYear() - 18, today.getMonth(), today.getDate() ); 
			 // Employee must be at least 18 years old
			 if (selectedDate > minimumDob) { showError( dob, "Employee must be at least 18 years old." ); isValid = false; 
				
			 } 
		 }
        }

        // Department validation
        if (department.value === "") {

            showError(
                department,
                "Please select a department."
            );

            isValid = false;
        }

        // Salary validation
        const salaryValue = parseFloat(salary.value);

        if (
            salary.value === "" ||
            isNaN(salaryValue) ||
            salaryValue <= 0
        ) {

            showError(
                salary,
                "Salary must be greater than zero."
            );

            isValid = false;
        }

        // Stop form submission if validation fails
        if (!isValid) {

            event.preventDefault();

            const firstError =
                form.querySelector(".client-error");

            if (firstError) {
                firstError.scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                });
            }

            return;
        }

        // Confirmation before saving
        const confirmed = confirm(
            "Are you sure you want to save this employee?"
        );

        if (!confirmed) {
            event.preventDefault();
        }

    });


    function showError(input, message) {

        const error = document.createElement("div");

        error.className = "error client-error";

        error.textContent = message;

        input.parentElement.appendChild(error);

        input.style.borderColor = "red";
    }


    function clearClientErrors() {

        const errors =
            form.querySelectorAll(".client-error");

        errors.forEach(function (error) {
            error.remove();
        });

        const inputs =
            form.querySelectorAll("input, select");

        inputs.forEach(function (input) {
            input.style.borderColor = "";
        });
    }


    // Manager dropdown behavior
    if (manager) {

        manager.addEventListener("change", function () {

            if (manager.value === "") {

                manager.style.backgroundColor = "#fff";

            } else {

                manager.style.backgroundColor = "#e8f5e9";
            }

        });
    }

});
