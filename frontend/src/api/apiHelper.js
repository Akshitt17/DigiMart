const BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function apiFetch(endpoint, options = {}) {
  const token = localStorage.getItem("jwt_token");

  const headers = {
    "Content-Type": "application/json", 
    ...options.headers,
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const url = `${BASE_URL}${endpoint}`;

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({
        message: `Request failed with status: ${response.status}`,
      }));
      const error = new Error(errorData.message || "API request failed");
      error.status = response.status;
      error.data = errorData;
      throw error;
    }

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      return await response.json();
    } else {
      const textResponse = await response.text();
      return { message: textResponse };
    }
  } catch (error) {
    console.error("API Fetch Error:", error);
    throw error;
  }
}

export default apiFetch;
