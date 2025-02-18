export const options = {
  plugins: {
    legend: {
      position: "bottom",
      labels: {
        usePointStyle: true,
      },
    },
    tooltip: {
      callbacks: {
        label: function (tooltipItem) {
          console.log("📈 Tooltip Data:", tooltipItem);
          return tooltipItem.raw;
        },
      },
    },
  },
  responsive: true,
  maintainAspectRatio: false,
};

export const dummy = [
  {
    date: 1,
    status: [180, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 2,
    status: [200, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 7,
    status: [300, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 8,
    status: [400, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 10,
    status: [500, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 15,
    status: [400, 59, 90, 81, 56, 55, 40],
  },
  {
    date: 20,
    status: [300, 59, 90, 81, 56, 55, 40],
  },
];
