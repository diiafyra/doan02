function logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('role');
    document.getElementById('menuDangNhap').style.display = 'block';
    document.getElementById('menuDangKy').style.display = 'block';
    document.getElementById('menuProfile').style.display = 'none';
    document.getElementById('menuDangXuat').style.display = 'none';

    window.location.href = '/api/public/';  // Đổi đường dẫn phù hợp
}
