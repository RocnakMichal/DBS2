function togglePasswordField() {
    const checkbox = document.getElementById('privateCheckbox');
    const field = document.getElementById('passwordField');
    field.style.display = checkbox.checked ? 'block' : 'none';
}

function validateGroupForm() {
    const isPrivate = document.getElementById('privateCheckbox').checked;
    const password = document.getElementById('joinPassword').value;

    if (isPrivate && password.trim() === '') {
        alert("Please enter a password for the private group.");
        return false; // prevent form submission
    }

    return true;
}