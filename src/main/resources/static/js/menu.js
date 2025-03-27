// Định nghĩa hàm navigateWithToken ở phạm vi toàn cục
function navigateWithToken(url) {
    const token = localStorage.getItem('jwt');

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'text/html' // Đặt Content-Type là text/html vì trả về HTML
        }
    })
        .then(response => {
            if (!response.ok) {
                alert('Bạn cần đăng nhập để truy cập!');
                window.location.href = '/api/public/login';
                return Promise.reject('Request failed');
            }
            return response.text(); // Lấy nội dung HTML dưới dạng text
        })
        .then(html => {
            // Thay đổi nội dung trang hiện tại bằng HTML nhận được
            document.open();
            document.write(html);
            document.close();
            // Cập nhật URL trên thanh địa chỉ mà không tải lại trang
            history.pushState({}, '', url);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra, vui lòng thử lại!');
        });
}

document.addEventListener("DOMContentLoaded", function() {
    var role = localStorage.getItem('role');
    if (!role || role == "null") role = "GUEST";
    var menu = document.getElementById('menu');
    var menuItems = [
        { text: "Trang Chủ", link: "/api/public/", showFor: ["USER", "GUEST"] },
        { text: "Đăng Ký", link: "/api/public/register", showFor: ["GUEST"] },
        { text: "Đăng Nhập", link: "/api/public/login", showFor: ["GUEST"] },
        { text: "Tài Khoản", link: "/api/user/profile", showFor: ["USER"] },
        { text: "Đăng Xuất", link: "javascript:logout()", showFor: ["USER"] }
    ];

    menuItems.forEach(item => {
        if (item.showFor.includes(role)) {
            var li = document.createElement("li");
            var a = document.createElement("a");
            a.textContent = item.text;

            if (item.text === "Đăng Xuất") {
                li.addEventListener("click", logout);
            } else {
                a.href = item.link;
                a.addEventListener("click", function(e) {
                    e.preventDefault();
                    navigateWithToken(item.link);
                });
            }

            li.appendChild(a);
            menu.appendChild(li);
        }
    });
});