import { RsData, PageDto, GetResMenuOrderDto,GetResDetailOrderDto} from "./types";
export async function paging(page: number, pageSize: number,email: string) {
    const url = new URL("http://localhost:8080/api/order/history");
    url.searchParams.append("page", page.toString());
    url.searchParams.append("pageSize", pageSize.toString());
    url.searchParams.append("email", email);
    const response = await fetch(url.toString(), {
        method: "GET",
        headers: { "Content-Type": "application/json" },
        
      });

    return response.json();
}