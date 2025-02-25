function logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('role');

    window.location.href = '/api/public/';  // Đổi đường dẫn phù hợp
}
