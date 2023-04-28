"use strict";

const formPopupWrapper = document.querySelector('.form-popup-wrapper');
const customersItemListDOM = document.querySelector(".order__items");
const nameInputDOM = formPopupWrapper.querySelector("#name");
const emailInputDOM = formPopupWrapper.querySelector("#email");
const roleSelectDOM = formPopupWrapper.querySelector("#role");
const fieldGroupPasswordDOM = formPopupWrapper.querySelector("#field-password");
const passwordInputDOM = formPopupWrapper.querySelector("#password");
const saveBtn = formPopupWrapper.querySelector("#btnSave");
const addBtn = document.querySelector("#btnAdd");
const cancelBtn = formPopupWrapper.querySelector("#btnCancel");
const deleteBtn = formPopupWrapper.querySelector("#btnDelete");
const formErrorMessage = formPopupWrapper.querySelector(".form-error-message");
const formTitle = formPopupWrapper.querySelector(".form-title");

const getCustomerById = async (id) => await fetch(
    `${ctx}/admin/customers?&action=getCustomerJson&customerId=${id}`,
    {
        method: "GET",
    }
);
    
customersItemListDOM.addEventListener("click", async (e) => {
        const customerItemDOM = e.target.closest(".order__item");
        if (!customerItemDOM) return;
        const customerId = customerItemDOM.dataset.customerId;
        fieldGroupPasswordDOM.classList.add("hidden");
        formTitle.innerHTML = "Edit customer";
        saveBtn.innerHTML = "Update";
        deleteBtn.classList.remove("hidden");

        formPopupWrapper.dataset.customerId = customerId;

        // fetch customer data
        const result = await getCustomerById(customerId);
        const data = await result.json();
        const {id, name, email, role} = data;

        // set form values
        nameInputDOM.value = name;
        emailInputDOM.value = email;
        roleSelectDOM.value = role;

        // show form popup
        formPopupWrapper.classList.add("active");
    }
);

cancelBtn.addEventListener("click", (e) => {
    e.preventDefault();
    formPopupWrapper.classList.remove("active");
});

saveBtn.addEventListener("click", async (e) => {
    try {
        e.preventDefault();
        const customerId = formPopupWrapper.dataset.customerId;
        const name = nameInputDOM.value;
        const email = emailInputDOM.value;
        const role = roleSelectDOM.value;
        const actionType = saveBtn.innerHTML;

        let res = null;
        if (actionType === "Update") {
            res = await fetch(
                `${ctx}/admin/customers?&action=saveCustomer&customerId=${customerId}`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        name,
                        email,
                        role,
                    }),
                });
        } else if (actionType === "Add") {
            const password = passwordInputDOM.value;
            res = await fetch(
                `${ctx}/admin/customers?&action=addCustomer`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        name,
                        email,
                        role,
                        password,
                    }),
                });
        }
        const data = await res.json();
        if (data.status === "success") {
            formPopupWrapper.classList.remove("active");
            window.location.reload();
        } else if (data.status === "error") {
            formErrorMessage.innerHTML = data.message;
        }
    } catch (e) {   
        console.error(e);
    }
});

addBtn.addEventListener("click", (e) => {
    e.preventDefault();
    fieldGroupPasswordDOM.classList.remove("hidden");
    formTitle.innerHTML = "Add new customer";
    formPopupWrapper.dataset.customerId = "";
    nameInputDOM.value = "";
    emailInputDOM.value = "";
    roleSelectDOM.value = "user";
    passwordInputDOM.value = "";
    formPopupWrapper.classList.add("active");
    saveBtn.innerHTML = "Add";
    deleteBtn.classList.add("hidden");
});

deleteBtn.addEventListener("click", async (e) => {
    try {
        e.preventDefault();
        const customerId = formPopupWrapper.dataset.customerId;
        const res = await fetch(
            `${ctx}/admin/customers?&action=deleteCustomer&customerId=${customerId}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        const data = await res.json();
        if(data.status === "success") {
            formPopupWrapper.classList.remove("active");
            window.location.reload();
        } else if (data.status === "error") {
            formErrorMessage.innerHTML = data.message;
        }
    } catch (e) {
        console.error(e);
    }
});


